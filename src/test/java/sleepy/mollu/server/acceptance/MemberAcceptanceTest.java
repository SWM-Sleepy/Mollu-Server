package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.member.emoji.controller.dto.SearchMyEmojiResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 사용자는_자신만의_이모티콘을_등록할_수_있다() {

        // given
        final String accessToken = 회원가입_요청_및_응답("google");
        내_이모티콘_등록_요청(accessToken, "emoticon1");

        // when
        final ExtractableResponse<Response> 내_이모티콘_조회_응답 = 내_이모티콘_조회_요청(accessToken);
        final SearchMyEmojiResponse response = toObject(내_이모티콘_조회_응답, SearchMyEmojiResponse.class);

        // then
        assertAll(
                () -> assertThat(내_이모티콘_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.emojis().get(0)).isNotBlank()
        );
    }
}
