package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionExistsResponse;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.time.LocalDateTime;
import java.util.List;
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
        final ExtractableResponse<Response> 컨텐츠_업로드_응답 = 컨텐츠_업로드_요청(accessToken, NOW, null);

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
    void 사용자는_댓글을_미리_볼_수_있다() {
        // given
        final Group group = 다른_사람과_내가_회원가입_이후_같은_그룹에_속한다();
        final String contentId = 컨텐츠를_업로드_한다(group.me, List.of(group.groupId));
        final String commentId = 컨텐츠에_댓글을_단다(group.other, contentId);

        // when
        final ExtractableResponse<Response> 댓글_미리_보기_응답 = 댓글_미리_보기_요청(group.me, contentId);

        // then
        assertAll(
                () -> assertThat(댓글_미리_보기_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("commentNum")).isEqualTo("1"),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("comment.commentId")).isEqualTo(commentId),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("comment.comment")).isEqualTo("김".repeat(200)),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("comment.molluId")).isEqualTo("mollu1"),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("comment.userName")).isEqualTo("name"),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("comment.profileSource")).isEmpty(),
                () -> assertThat(댓글_미리_보기_응답.jsonPath().getString("comment.createdAt")).isNotEmpty()
        );
    }

    private Group 다른_사람과_내가_회원가입_이후_같은_그룹에_속한다() {
        final String me = 회원가입_요청_및_응답("google");
        final String groupId = getGroupId(그룹_생성_요청(me));
        final String code = 그룹_초대_코드_조회_요청_및_응답(me, groupId);
        final String other = 다른_사람_회원가입_요청_및_응답("google");
        초대_코드로_그룹에_참여한다(other, code);

        return new Group(groupId, me, other);
    }

    private String 다른_사람이_그룹에_참여_후_댓글을_단다(String code, String contentId) {
        final String other = 다른_사람_회원가입_요청_및_응답("google");
        초대_코드로_그룹에_참여한다(other, code);
        final String commentId = getCommentId(댓글_등록_요청(other, contentId));
        return commentId;
    }

    private String 그룹_초대_코드_조회_요청_및_응답(String accessToken, String groupId) {
        final ExtractableResponse<Response> 그룹_초대_코드_조회_응답 = 그룹_초대_코드_조회_요청(accessToken, groupId);
        final SearchGroupCodeResponse response = toObject(그룹_초대_코드_조회_응답, SearchGroupCodeResponse.class);
        return response.code();
    }

    private void 초대_코드로_그룹에_참여한다(String accessToken, String code) {
        final ExtractableResponse<Response> response = 초대_코드로_그룹_참여_요청(accessToken, code);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void MOLLU_타임_이전에_컨텐츠를_업로드한다(String accessToken) {
        MOLLU_타임_설정();
        컨텐츠_업로드_요청(accessToken, NOW.minusHours(2), null);
    }

    private void MOLLU_타임_이후에_컨텐츠를_업로드한다(String accessToken) {
        MOLLU_타임_설정();
        컨텐츠_업로드_요청(accessToken, NOW, null);
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

    private String 컨텐츠를_업로드_한다(String accessToken) {
        final ExtractableResponse<Response> 컨텐츠_업로드_응답 = 컨텐츠_업로드_요청(accessToken);
        assertThat(컨텐츠_업로드_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return getContentId(컨텐츠_업로드_응답);
    }

    private String 컨텐츠를_업로드_한다(String accessToken, List<String> groups) {
        final ExtractableResponse<Response> 컨텐츠_업로드_응답 = 컨텐츠_업로드_요청(accessToken, groups);
        assertThat(컨텐츠_업로드_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return getContentId(컨텐츠_업로드_응답);
    }

    private void 먼저_컨텐츠에_반응을_남겼는지_확인한다(String accessToken, String contentId) {
        final ExtractableResponse<Response> 컨텐츠_반응_여부_조회_응답 = 컨텐츠_반응_여부_조회_요청(accessToken, contentId);
        final SearchReactionExistsResponse response = toObject(컨텐츠_반응_여부_조회_응답, SearchReactionExistsResponse.class);

        assertAll(
                () -> assertThat(컨텐츠_반응_여부_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.reacted()).isFalse()
        );
    }

    private String 컨텐츠에_반응을_추가한다(String accessToken, String contentId) {
        final ExtractableResponse<Response> 컨텐츠_반응_추가_응답 = 컨텐츠_반응_추가_요청(accessToken, contentId);
        assertThat(컨텐츠_반응_추가_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return getReactionId(컨텐츠_반응_추가_응답);
    }

    private void 컨텐츠의_반응을_조회한다(String accessToken, String contentId) {
        final ExtractableResponse<Response> 컨텐츠_반응_조회_응답 = 컨텐츠_반응_조회_요청(accessToken, contentId);
        final SearchReactionResponse response = toObject(컨텐츠_반응_조회_응답, SearchReactionResponse.class);

        assertAll(
                () -> assertThat(컨텐츠_반응_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.reactions()).hasSize(1)
        );
    }

    private String 다른_사람이_컨텐츠를_업로드_한다() {
        final String other = 회원가입_및_디폴트_그룹_가입_요청_및_응답("other", "mollu_id2");
        return getContentId(컨텐츠_업로드_요청(other));
    }

    private String 컨텐츠에_댓글을_단다(String accessToken, String contentId) {
        final ExtractableResponse<Response> 댓글_작성_응답 = 댓글_등록_요청(accessToken, contentId);
        final String commentId = getCommentId(댓글_작성_응답);

        assertAll(
                () -> assertThat(댓글_작성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(commentId).isNotNull()
        );

        return commentId;
    }

    private void 컨텐츠의_댓글을_조회한다(String accessToken, String contentId) {
        final ExtractableResponse<Response> 댓글_조회_응답 = 댓글_조회_요청(accessToken, contentId);
        final SearchCommentResponse response = toObject(댓글_조회_응답, SearchCommentResponse.class);

        assertAll(
                () -> assertThat(댓글_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.comments()).hasSize(1)
        );
    }

    private String 다른_사람이_컨텐츠에_댓글을_단다(String contentId) {
        final String other = 회원가입_및_디폴트_그룹_가입_요청_및_응답("memberId2", "mollu_id2");
        return 컨텐츠에_댓글을_단다(other, contentId);
    }

    private record Group(String groupId, String me, String other) {

    }

    @Nested
    class 회원가입_시_디폴트_그룹에_참여한다면_ {

        @Test
        @DisplayName("신고 컨텐츠 조회 불가 v1.0")
        void 신고_컨텐츠_조회_불가() {
            // given
            final String other = 회원가입_및_디폴트_그룹_가입_요청_및_응답("other", "mollu_id1");
            final String contentId = getContentId(컨텐츠_업로드_요청(other, NOW, null));
            final String me = 회원가입_및_디폴트_그룹_가입_요청_및_응답("me", "mollu_id2");

            final ExtractableResponse<Response> 그룹원_피드_조회_응답 = 그룹원_피드_조회_요청(me);
            final GroupSearchFeedResponse response2 = toObject(그룹원_피드_조회_응답, GroupSearchFeedResponse.class);

            assertThat(response2.feed()).isNotEmpty();

            // when
            컨텐츠_신고_요청(me, contentId);

            // then
            final ExtractableResponse<Response> 신고_이후_그룹원_피드_조회_응답 = 그룹원_피드_조회_요청(me);
            final GroupSearchFeedResponse response3 = toObject(신고_이후_그룹원_피드_조회_응답, GroupSearchFeedResponse.class);

            assertAll(
                    () -> assertThat(신고_이후_그룹원_피드_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(response3.feed()).isEmpty()
            );
        }

        @Test
        @DisplayName("사용자는 컨텐츠에 자신만의 이모티콘으로 반응을 남길 수 있다 v1.0")
        void 사용자는_컨텐츠에_자신만의_이모티콘으로_반응을_남길_수_있다() {
            // given
            final String emojiType = "emoticon1";
            final String accessToken = 회원가입_및_디폴트_그룹_가입_요청_및_응답("memberId", "mollu_id");

            내_이모티콘_등록_요청(accessToken, emojiType);

            final String contentId = 컨텐츠를_업로드_한다(accessToken);

            // when
            먼저_컨텐츠에_반응을_남겼는지_확인한다(accessToken, contentId);
            final String reactionId = 컨텐츠에_반응을_추가한다(accessToken, contentId);
            컨텐츠의_반응을_조회한다(accessToken, contentId);
            final ExtractableResponse<Response> 컨텐츠_반응_삭제_응답 = 컨텐츠_반응_삭제_요청(accessToken, contentId, reactionId);

            // then
            assertThat(컨텐츠_반응_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("사용자는 컨텐츠에 댓글을 작성할 수 있다 v1.0")
        void 사용자는_컨텐츠에_댓글을_작성할_수_있다() {
            // given
            final String accessToken = 회원가입_및_디폴트_그룹_가입_요청_및_응답("memberId1", "mollu_id1");
            final String contentId = 다른_사람이_컨텐츠를_업로드_한다();

            // when
            final String commentId = 컨텐츠에_댓글을_단다(accessToken, contentId);
            컨텐츠의_댓글을_조회한다(accessToken, contentId);
            final ExtractableResponse<Response> 댓글_삭제_요청 = 댓글_삭제_요청(accessToken, contentId, commentId);

            // then
            assertThat(댓글_삭제_요청.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("사용자는 댓글을 신고할 수 있다 v1.0")
        void 사용자는_댓글을_신고할_수_있다() {
            // given
            final String me = 회원가입_및_디폴트_그룹_가입_요청_및_응답("memberId1", "mollu_id1");
            final String contentId = getContentId(컨텐츠_업로드_요청(me));
            final String commentId = 다른_사람이_컨텐츠에_댓글을_단다(contentId);

            // when
            final ExtractableResponse<Response> 댓글_신고_응답 = 댓글_신고_요청(me, contentId, commentId);
            assertThat(댓글_신고_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            final SearchCommentResponse 댓글_조회_응답 = toObject(댓글_조회_요청(me, contentId), SearchCommentResponse.class);

            // then
            assertThat(댓글_조회_응답.comments()).isEmpty();
        }
    }

    @Nested
    class 회원가입_시_디폴트_그룹에_참여하지_않는다면_ {

        @Test
        @DisplayName("신고 컨텐츠 조회 불가 v1.1")
        void 신고_컨텐츠_조회_불가() {
            // given
            final String other = 다른_사람_회원가입_요청_및_응답("google");
            final String me = 회원가입_요청_및_응답("google");

            final String groupId = getGroupId(그룹_생성_요청(other));
            final String code = 그룹_초대_코드_조회_요청_및_응답(other, groupId);
            final String contentId = getContentId(컨텐츠_업로드_요청(other, List.of(groupId)));
            초대_코드로_그룹에_참여한다(me, code);

            final ExtractableResponse<Response> 그룹원_피드_조회_응답 = 그룹원_피드_조회_요청(me);
            final GroupSearchFeedResponse response2 = toObject(그룹원_피드_조회_응답, GroupSearchFeedResponse.class);

            assertThat(response2.feed()).isNotEmpty();

            // when
            컨텐츠_신고_요청(me, contentId);

            // then
            final ExtractableResponse<Response> 신고_이후_그룹원_피드_조회_응답 = 그룹원_피드_조회_요청(me);
            final GroupSearchFeedResponse response3 = toObject(신고_이후_그룹원_피드_조회_응답, GroupSearchFeedResponse.class);

            assertAll(
                    () -> assertThat(신고_이후_그룹원_피드_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                    () -> assertThat(response3.feed()).isEmpty()
            );
        }

        @Test
        @DisplayName("사용자는 컨텐츠에 자신만의 이모티콘으로 반응을 남길 수 있다 v1.1")
        void 사용자는_컨텐츠에_자신만의_이모티콘으로_반응을_남길_수_있다() {
            // given
            final String emojiType = "emoticon1";
            final String accessToken = 회원가입_요청_및_응답("google");
            final String groupId = getGroupId(그룹_생성_요청(accessToken));

            내_이모티콘_등록_요청(accessToken, emojiType);

            final String contentId = getContentId(컨텐츠_업로드_요청(accessToken, List.of(groupId)));

            // when
            먼저_컨텐츠에_반응을_남겼는지_확인한다(accessToken, contentId);
            final String reactionId = 컨텐츠에_반응을_추가한다(accessToken, contentId);
            컨텐츠의_반응을_조회한다(accessToken, contentId);
            final ExtractableResponse<Response> 컨텐츠_반응_삭제_응답 = 컨텐츠_반응_삭제_요청(accessToken, contentId, reactionId);

            // then
            assertThat(컨텐츠_반응_삭제_응답.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("사용자는 컨텐츠에 댓글을 작성할 수 있다 v1.1")
        void 사용자는_컨텐츠에_댓글을_작성할_수_있다() {
            // given
            final String accessToken = 회원가입_요청_및_응답("google");
            final String groupId = getGroupId(그룹_생성_요청(accessToken));
            final String contentId = getContentId(컨텐츠_업로드_요청(accessToken, List.of(groupId)));

            // when
            final String commentId = 컨텐츠에_댓글을_단다(accessToken, contentId);
            컨텐츠의_댓글을_조회한다(accessToken, contentId);
            final ExtractableResponse<Response> 댓글_삭제_요청 = 댓글_삭제_요청(accessToken, contentId, commentId);

            // then
            assertThat(댓글_삭제_요청.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("사용자는 댓글을 신고할 수 있다 v1.1")
        void 사용자는_댓글을_신고할_수_있다() {
            // given
            final String me = 회원가입_요청_및_응답("google");
            final String groupId = getGroupId(그룹_생성_요청(me));
            final String code = 그룹_초대_코드_조회_요청_및_응답(me, groupId);
            final String contentId = getContentId(컨텐츠_업로드_요청(me, List.of(groupId)));

            final String commentId = 다른_사람이_그룹에_참여_후_댓글을_단다(code, contentId);

            // when
            final ExtractableResponse<Response> 댓글_신고_응답 = 댓글_신고_요청(me, contentId, commentId);
            assertThat(댓글_신고_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());

            final SearchCommentResponse 댓글_조회_응답 = toObject(댓글_조회_요청(me, contentId), SearchCommentResponse.class);

            // then
            assertThat(댓글_조회_응답.comments()).isEmpty();
        }
    }
}
