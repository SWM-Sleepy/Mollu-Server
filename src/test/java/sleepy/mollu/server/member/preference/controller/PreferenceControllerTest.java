package sleepy.mollu.server.member.preference.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.member.preference.dto.PhoneTokenRequest;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PreferenceControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[알림 설정 변경 API 호출시] ")
    class UpdatePreferenceTest {

        @Test
        @DisplayName("요청 body에 null이 포함되어 있으면 400을 응답한다.")
        void updatePreferenceTest() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions result = put("/members/preference", accessToken);

            // then
            result.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("200을 응답한다.")
        void updatePreferenceTest2() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final PreferenceRequest request = new PreferenceRequest(true, true);

            // when
            final ResultActions result = put("/members/preference", accessToken, request);

            // then
            result.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[알림 설정 조회 API 호출시] ")
    class UpdatePhoneToken {

        @Test
        @DisplayName("phoneToken이 설정되어있지 않으면 400을 응답한다.")
        void UpdatePhoneToken() throws Exception {
            // given & when
            final ResultActions resultActions = post("/members/preference/token");

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("201을 응답한다")
        void UpdatePhoneToken1() throws Exception {
            // given
            final PhoneTokenRequest request = new PhoneTokenRequest("phoneToken");

            // when
            final ResultActions resultActions = post("/members/preference/token", request);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }
}