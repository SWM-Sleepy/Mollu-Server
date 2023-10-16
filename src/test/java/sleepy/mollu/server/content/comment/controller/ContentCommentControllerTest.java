package sleepy.mollu.server.content.comment.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentPreviewResponse;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentPreviewResponse.CommentResponse;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sleepy.mollu.server.fixture.AcceptanceFixture.댓글_등록_요청_데이터;

class ContentCommentControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[댓글 등록 API 호출시] ")
    class CreateComment {

        @Test
        @DisplayName("댓글 내용을 적지 않았으면 400을 반환한다.")
        void CreateComment0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/contents/contentId/comments", accessToken);

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("댓글을 적었으면 201을 반환한다.")
        void CreateComment() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/contents/contentId/comments", accessToken, 댓글_등록_요청_데이터);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[댓글 미리 보기 API 호출시] ")
    class SearchCommentPreview {

        @Test
        @DisplayName("댓글이 없으면 comment 필드가 비어 있어야 한다.")
        void SearchCommentPreview0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            given(contentCommentService.searchCommentPreview("memberId", "contentId"))
                    .willReturn(new SearchCommentPreviewResponse(0L, null));

            // when
            final ResultActions resultActions = get("/contents/contentId/comments/preview", accessToken);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.comment").doesNotExist())
                    .andDo(print());
        }

        @Test
        @DisplayName("댓글이 있다면 comment 필드가 있어야 한다.")
        void SearchCommentPreview1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            given(contentCommentService.searchCommentPreview("memberId", "contentId"))
                    .willReturn(new SearchCommentPreviewResponse(1L, mock(CommentResponse.class)));

            // when
            final ResultActions resultActions = get("/contents/contentId/comments/preview", accessToken);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.comment").exists())
                    .andDo(print());
        }
    }
}