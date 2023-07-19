package sleepy.mollu.server.member.preference.controller;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.service.PreferenceService;
import sleepy.mollu.server.oauth2.config.CustomJwtConfig;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PreferenceController.class)
@Import(CustomJwtConfig.class)
class PreferenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PreferenceService preferenceService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("[알림 설정 변경 API 호출시] ")
    class UpdatePreferenceTest {

        @Test
        @DisplayName("요청 body에 null이 포함되어 있으면 400을 응답한다.")
        void updatePreferenceTest() throws Exception {
            // given
            final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
            final String accessToken = jwtToken.accessToken();
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            // when
            final ResultActions result = mockMvc.perform(put("/members/preference")
                    .headers(headers));

            // then
            result.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("200을 응답한다.")
        void updatePreferenceTest2() throws Exception {
            // given
            final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
            final String accessToken = jwtToken.accessToken();
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            final PreferenceRequest request = new PreferenceRequest(true, true);
            final String body = objectMapper.writeValueAsString(request);

            // when
            final ResultActions result = mockMvc.perform(put("/members/preference")
                    .headers(headers)
                    .contentType("application/json")
                    .content(body));

            // then
            result.andExpect(status().isOk())
                    .andDo(print());
        }
    }
}