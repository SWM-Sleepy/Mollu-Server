package sleepy.mollu.server.oauth2.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuth2ControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[소셜 로그인 API 호출 시] ")
    class SocialLoginTest {

        @Test
        @DisplayName("파라미터를 설정하지 않으면 다른 API를 호출하므로 404를 반환한다")
        void OAuth2ControllerTest() throws Exception {
            // given & when
            final ResultActions resultActions = post("/auth/login");

            // then
            resultActions.andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        @DisplayName("호출에 성공하면 201을 반환한다")
        void OAuth2ControllerTest2() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/auth/login/google", accessToken);

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
            final String accessToken = getAccessToken("memberId");
            final SignupRequest request = new SignupRequest("김준형", LocalDate.now(), "molluId", "phoneToken");

            // when
            final ResultActions resultActions = post("/auth/signup/google", accessToken, request);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[아이디 중복 확인 API 호출 시] ")
    class CheckIdTest {

        @Test
        @DisplayName("파라미터를 설정하지 않으면 400을 반환한다")
        void checkIdTest() throws Exception {
            // given & when
            final ResultActions resultActions = get("/auth/check-id");

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("호출에 성공하면 200을 반환한다")
        void checkIdTest1() throws Exception {
            // given & when
            String molluId = "molluId";
            final ResultActions resultActions = get("/auth/check-id?molluId=" + molluId);

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[토큰 재발급 API 호출 시] ")
    class RefreshTest {

        @Test
        @DisplayName("리프레시 토큰이 없으면 400을 반환한다")
        void RefreshTest1() throws Exception {
            // given & when
            final ResultActions resultActions = post("/auth/refresh");

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("유효한 리프레시 토큰이라면 accessToken과 refreshToken을 응답하고 201을 반환한다")
        void RefreshTest() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final String newAccessToken = "newAccessToken";
            final String newRefreshToken = "newRefreshToken";

            given(oauth2Service.refresh(anyString())).willReturn(new TokenResponse(newAccessToken, newRefreshToken));

            // when
            final ResultActions resultActions = post("/auth/refresh", accessToken);

            // then
            resultActions.andExpect(status().isCreated())
                    .andExpect(result -> {
                        final String responseBody = result.getResponse().getContentAsString();
                        final TokenResponse tokenResponse = objectMapper.readValue(responseBody, TokenResponse.class);

                        assertThat(tokenResponse.accessToken()).isEqualTo(newAccessToken);
                        assertThat(tokenResponse.refreshToken()).isEqualTo(newRefreshToken);
                    })
                    .andDo(print());
        }
    }
}