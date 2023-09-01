package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 사용자는_자신만의_이모티콘을_등록할_수_있다() {

        // given
        final String accessToken = 회원가입_요청_및_응답("google");

        // when
        final ExtractableResponse<Response> response = 내_이모티콘_등록_요청(accessToken);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
