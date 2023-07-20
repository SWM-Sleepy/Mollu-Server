package sleepy.mollu.server.member.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.partyrun.jwtmanager.JwtGenerator;
import online.partyrun.jwtmanager.dto.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.member.profile.service.ProfileService;
import sleepy.mollu.server.oauth2.config.CustomJwtConfig;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@Import(CustomJwtConfig.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("[프로필 수정 API 호출 시] ")
    class UpdateProfileTest {

        @Test
        @DisplayName("일부 데이터만 넣어서 전송해도 200을 반환한다.")
        void updateProfileTest() throws Exception {
            // given
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("molluId", "ysyss");

            final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
            final String accessToken = jwtToken.accessToken();
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            // when
            final ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PATCH, "/members/profile")
                    .headers(headers)
                    .params(params));

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }
    }

}