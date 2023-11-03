package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.util.List;

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
    void 회원가입_시_디폴트_그룹에_속한다() {
        // given
        final String accessToken = 회원가입_요청_및_응답("google");

        // when
        final ExtractableResponse<Response> 소속_그룹_응답 = 소속_그룹_조회(accessToken);

        // then
        assertAll(
                () -> assertThat(소속_그룹_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(소속_그룹_응답.body().jsonPath().getList("groups")).hasSize(1),
                () -> assertThat(소속_그룹_응답.body().jsonPath().getList("groups.groupId")).isNotNull(),
                () -> assertThat(소속_그룹_응답.body().jsonPath().getList("groups.groupName")).isEqualTo(List.of("디폴트 그룹")),
                () -> assertThat(소속_그룹_응답.body().jsonPath().getList("groups.groupMemberCount")).isEqualTo(List.of(1))
        );
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

    @Test
    void 회원탈퇴() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);

        // when
        final ExtractableResponse<Response> 회원탈퇴_응답 = 회원탈퇴_요청(response.accessToken());

        // then
        final ExtractableResponse<Response> 프로필_조회_응답 = 프로필_조회_요청(response.accessToken());

        assertAll(
                () -> assertThat(회원탈퇴_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(프로필_조회_응답.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value())
        );
    }
}
