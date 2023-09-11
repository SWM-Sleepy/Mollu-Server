package sleepy.mollu.server.content.report.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.content.report.domain.CommentReport;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ContentReportControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[컨텐츠 신고 API 호출시] ")
    class ReportContentTest {

        @Test
        @DisplayName("신고 사유를 적지 않으면 `reason`에 빈 문자열이 저장되고, 201을 응답한다.")
        void reportContentTest() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final String contentId = "contentId";

            // when
            final ResultActions result = post("/contents/" + contentId + "/report", accessToken);

            // then
            result.andExpect(status().isCreated())
                    .andDo(print());
            then(reportService).should().reportContent("memberId", "contentId", "");
        }
    }

    @Nested
    @DisplayName("[댓글 신고 API 호출시] ")
    class ReportCommentTest {

        final CommentReport commentReport = mock(CommentReport.class);

        @Test
        @DisplayName("신고 사유를 적지 않아도 201을 응답한다.")
        void reportCommentTest() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            given(reportService.reportComment("memberId", "contentId", "commentId", ""))
                    .willReturn(commentReport);
            given(commentReport.getId()).willReturn(1L);

            // when
            final ResultActions result = post("/contents/contentId/comments/commentId/report", accessToken);

            // then
            result.andExpect(status().isCreated())
                    .andDo(print());
        }
    }
}