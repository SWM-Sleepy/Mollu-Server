package sleepy.mollu.server.content.reaction.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sleepy.mollu.server.fixture.AcceptanceFixture.컨텐츠_반응_추가_요청_데이터;

class ContentReactionControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[컨텐츠 반응 등록 API 호출시 ]")
    class ContentReactionControllerTest0 {

        @Test
        @DisplayName("반응 타입을 명시하지 않으면 400을 응답한다.")
        void ContentReactionControllerTest0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/contents/contentId/reactions", accessToken);

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("파라미터가 모두 설정되었으면 201을 응답한다.")
        void ContentReactionControllerTest1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/contents/contentId/reactions", accessToken, 컨텐츠_반응_추가_요청_데이터);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }
}