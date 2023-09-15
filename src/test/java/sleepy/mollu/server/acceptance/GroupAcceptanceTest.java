package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class GroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 사용자는_그룹을_생성할_수_있다() {
        // given
        final String accessToken = 회원가입_요청_및_응답("google");

        // when
        final ExtractableResponse<Response> response = 그룹_생성_요청(accessToken);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(getGroupId(response)).isNotNull()
        );
    }
}
