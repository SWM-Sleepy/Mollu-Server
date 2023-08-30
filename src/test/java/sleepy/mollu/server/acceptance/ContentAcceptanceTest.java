package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class ContentAcceptanceTest extends AcceptanceTest {

    @Test
    void 회원가입_이후_컨텐츠_업로드() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        final String accessToken = response.accessToken();

        final ExtractableResponse<Response> mollu_타임_조회_응답 = MOLLU_타임_조회_요청(accessToken);
        final SearchMolluTimeResponse response1 = toObject(mollu_타임_조회_응답, SearchMolluTimeResponse.class);
        final LocalDateTime molluTime = response1.molluTime();
        final String question = response1.question();

        assertAll(
                () -> assertThat(molluTime).isNull(),
                () -> assertThat(question).isNull()
        );

        // when
        final ExtractableResponse<Response> 컨텐츠_업로드_응답 = 컨텐츠_업로드_요청(accessToken, NOW);

        // then
        assertAll(
                () -> assertThat(컨텐츠_업로드_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(컨텐츠_업로드_응답.header("Location")).isNotNull()
        );
    }

    @Test
    void MOLLU_타임_이전_컨텐츠_업로드_이후_MOLLU_타임_조회() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        final String accessToken = response.accessToken();

        MOLLU_타임_이전에_컨텐츠를_업로드한다(accessToken);

        // when
        final SearchMolluTimeResponse mollu_타임_조회_응답 = toObject(MOLLU_타임_조회_요청(accessToken), SearchMolluTimeResponse.class);

        // then
        assertAll(
                () -> assertThat(mollu_타임_조회_응답.molluTime()).isNotNull(),
                () -> assertThat(mollu_타임_조회_응답.question()).isNotNull()
        );
    }

    @Test
    void MOLLU_타임_이후의_컨텐츠_업로드_이후_MOLLU_타임_조회() {
        // given
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청("google");
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        final String accessToken = response.accessToken();

        MOLLU_타임_이후에_컨텐츠를_업로드한다(accessToken);

        // when
        final SearchMolluTimeResponse mollu_타임_조회_응답 = toObject(MOLLU_타임_조회_요청(accessToken), SearchMolluTimeResponse.class);

        // then
        assertAll(
                () -> assertThat(mollu_타임_조회_응답.molluTime()).isNull(),
                () -> assertThat(mollu_타임_조회_응답.question()).isNull()
        );
    }

    @Test
    void 신고_컨텐츠_조회_불가() {
        // given
        final ExtractableResponse<Response> 다른_사람_회원가입_응답 = 다른_사람_회원가입_요청("google");
        final TokenResponse response = toObject(다른_사람_회원가입_응답, TokenResponse.class);

        final String contentId = getContentId(컨텐츠_업로드_요청(response.accessToken(), NOW));

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

    private void MOLLU_타임_이전에_컨텐츠를_업로드한다(String accessToken) {
        MOLLU_타임_설정();
        컨텐츠_업로드_요청(accessToken, NOW.minusHours(2));
    }

    private void MOLLU_타임_이후에_컨텐츠를_업로드한다(String accessToken) {
        MOLLU_타임_설정();
        컨텐츠_업로드_요청(accessToken, NOW);
    }

    private void MOLLU_타임_설정() {
        given(molluAlarmRepository.findTop()).willReturn(Optional.of(MolluAlarm.builder()
                .molluTime(NOW.plusDays(1))
                .question("question")
                .send(false)
                .build()));

        given(molluAlarmRepository.findSecondTop()).willReturn(Optional.of(MolluAlarm.builder()
                .molluTime(NOW.minusHours(1))
                .question("question")
                .send(true)
                .build()));
    }
}
