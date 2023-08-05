package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    void 구글_로그인() {
        // given & when
        final ExtractableResponse<Response> 구글_로그인_응답 = 로그인_요청("google");

        // then
        assertThat(구글_로그인_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 애플_로그인() {
        // given & when
        final ExtractableResponse<Response> 애플_로그인_응답 = 로그인_요청("apple");

        // then
        assertThat(애플_로그인_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void 구글_회원가입() {
        // given
        아이디_중복_확인_요청();

        // when
        final ExtractableResponse<Response> 구글_회원가입_응답 = 회원가입_요청("google");

        // then
        assertThat(구글_회원가입_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 애플_회원가입() {
        // given
        아이디_중복_확인_요청();

        // when
        final ExtractableResponse<Response> 애플_회원가입_응답 = 회원가입_요청("apple");

        // then
        assertThat(애플_회원가입_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void 토큰_재발급() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        final String refreshToken = response.refreshToken();

        // when
        final ExtractableResponse<Response> 토큰_재발급_응답 = 토큰_재발급_요청(refreshToken);
        final TokenResponse response1 = toObject(토큰_재발급_응답, TokenResponse.class);


        // then
        assertAll(
                () -> assertThat(토큰_재발급_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response1.accessToken()).isNotNull(),
                () -> assertThat(response1.refreshToken()).isNotNull()
        );
    }
}
