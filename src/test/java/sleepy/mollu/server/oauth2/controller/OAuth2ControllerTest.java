package sleepy.mollu.server.oauth2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.oauth2.config.CustomJwtConfig;
import sleepy.mollu.server.oauth2.service.OAuth2Service;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OAuth2Controller.class)
@Import(CustomJwtConfig.class)
class OAuth2ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OAuth2Service oauth2Service;

    @Nested
    @DisplayName("[소셜 로그인 API 호출 시] ")
    class SocialLoginTest {

        @Test
        @DisplayName("파라미터를 설정하지 않으면 다른 API를 호출하므로 404를 반환한다")
        void OAuth2ControllerTest() throws Exception {
            // given & when
            final ResultActions resultActions = mockMvc.perform(post("/auth/login"));

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @DisplayName("호출에 성공하면 201을 반환한다")
        void OAuth2ControllerTest2() throws Exception {
            // given
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + "test_token");

            // when
            final ResultActions resultActions = mockMvc.perform(post("/auth/login/google")
                    .headers(headers));

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[소셜 회원가입 API 호출 시] ")
    class SocialSignupTest {

        @Test
        @DisplayName("호출에 성공하면 201을 반환한다")
        void socialSignupTest1() throws Exception {
            // given

            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + "test_token");

            final SignupRequest request = new SignupRequest("김준형", LocalDate.now(), "molluId", "phoneToken");
            final String requestBody = objectMapper.writeValueAsString(request);

            // when
            final ResultActions resultActions = mockMvc.perform(post("/auth/signup/google")
                    .headers(headers)
                    .contentType("application/json")
                    .content(requestBody));

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }
}