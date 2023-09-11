package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.member.emoji.controller.dto.SearchMyEmojiResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    void 사용자는_자신만의_이모티콘을_등록할_수_있다() {
        // given
        final String emojiType = "emoticon1";
        final String accessToken = 회원가입_요청_및_응답("google");
        내_이모티콘_등록_요청(accessToken, emojiType);

        final SearchMyEmojiResponse response = toObject(내_이모티콘_조회_요청(accessToken), SearchMyEmojiResponse.class);
        assertThat(response.emojis().get(0)).isNotBlank();

        // when
        final ExtractableResponse<Response> 내_이모티콘_삭제_응답 = 내_이모티콘_삭제_요청(accessToken, emojiType);
        assertThat(내_이모티콘_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

        // then
        final SearchMyEmojiResponse response2 = toObject(내_이모티콘_조회_요청(accessToken), SearchMyEmojiResponse.class);
        assertThat(response2.emojis().get(0)).isBlank();
    }

    @Test
    void 앱을_실행할_때_마다_클라이언트는_알림_토큰을_서버로_보낸다() {
        // given
        final String accessToken = 회원가입_요청_및_응답("google");

        // when
        final ExtractableResponse<Response> 알림_토큰_설정_응답 = 알림_토큰_설정_요청(accessToken);

        // then
        assertThat(알림_토큰_설정_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
