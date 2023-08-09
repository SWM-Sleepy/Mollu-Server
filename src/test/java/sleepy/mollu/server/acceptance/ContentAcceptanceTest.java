package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class ContentAcceptanceTest extends AcceptanceTest {

    @Test
    void 회원가입_이후_컨텐츠_업로드() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        final String accessToken = response.accessToken();

        회원가입_이후_첫_번째_촬영임을_가정();
        final ExtractableResponse<Response> mollu_타임_조회_응답 = MOLLU_타임_조회_요청(accessToken);
        final SearchMolluTimeResponse response1 = toObject(mollu_타임_조회_응답, SearchMolluTimeResponse.class);
        final LocalDateTime molluTime = response1.molluTime();
        final String question = response1.question();

        assertAll(
                () -> assertThat(molluTime).isNull(),
                () -> assertThat(question).isNull()
        );

        // when
        final ExtractableResponse<Response> 컨텐츠_업로드_응답 = 컨텐츠_업로드_요청(accessToken);

        // then
        assertAll(
                () -> assertThat(컨텐츠_업로드_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(컨텐츠_업로드_응답.header("Location")).isNotNull()
        );
    }

    @Test
    void MOLLU_타임_컨텐츠_업로드() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        final String accessToken = response.accessToken();

        MOLLU_타임_이후_첫_번째_촬영임을_가정();
        final ExtractableResponse<Response> mollu_타임_조회_응답 = MOLLU_타임_조회_요청(accessToken);
        final SearchMolluTimeResponse response1 = toObject(mollu_타임_조회_응답, SearchMolluTimeResponse.class);
        final LocalDateTime molluTime = response1.molluTime();
        final String question = response1.question();

        assertAll(
                () -> assertThat(molluTime).isNotNull(),
                () -> assertThat(question).isNotNull()
        );

        // when
        final ExtractableResponse<Response> 컨텐츠_업로드_응답 = 컨텐츠_업로드_요청(accessToken, molluTime, question);

        // then
        assertAll(
                () -> assertThat(컨텐츠_업로드_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(컨텐츠_업로드_응답.header("Location")).isNotNull()
        );
    }

    @Test
    void 신고_컨텐츠_조회_불가() {
        // given
        final ExtractableResponse<Response> 다른_사람_회원가입_응답 = 다른_사람_회원가입_요청("google");
        final TokenResponse response = toObject(다른_사람_회원가입_응답, TokenResponse.class);

        final String contentId = getContentId(컨텐츠_업로드_요청(response.accessToken()));

        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response1 = toObject(회원가입_응답, TokenResponse.class);
        final String accessToken = response1.accessToken();

        final ExtractableResponse<Response> 그룹원_피드_조회_응답 = 그룹원_피드_조회_요청(accessToken);
        final GroupSearchFeedResponse response2 = toObject(그룹원_피드_조회_응답, GroupSearchFeedResponse.class);

        assertThat(response2.feed()).isNotEmpty();

        // when
        컨텐츠_신고_요청(accessToken, contentId);

        // then
        final ExtractableResponse<Response> 신고_이후_그룹원_피드_조회_응답 = 그룹원_피드_조회_요청(accessToken);
        final GroupSearchFeedResponse response3 = toObject(신고_이후_그룹원_피드_조회_응답, GroupSearchFeedResponse.class);

        assertAll(
                () -> assertThat(신고_이후_그룹원_피드_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response3.feed()).isEmpty()
        );

    }

    private void 회원가입_이후_첫_번째_촬영임을_가정() {
        given(molluTimeService.searchMolluTime(anyString())).willReturn(new SearchMolluTimeResponse(null, null));
    }

    private void MOLLU_타임_이후_첫_번째_촬영임을_가정() {
        given(molluTimeService.searchMolluTime(anyString())).willReturn(new SearchMolluTimeResponse(LocalDateTime.now(), "질문"));
    }
}
