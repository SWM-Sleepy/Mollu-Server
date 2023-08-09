package sleepy.mollu.server.member.profile.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.ControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProfileControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[프로필 수정 API 호출 시] ")
    class UpdateProfileTest {

        @Test
        @DisplayName("일부 데이터만 넣어서 전송해도 200을 반환한다.")
        void updateProfileTest() throws Exception {
            // given
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("molluId", "ysyss");

            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = multipart(HttpMethod.PATCH, "/members/profile", accessToken, params);

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }
    }

}