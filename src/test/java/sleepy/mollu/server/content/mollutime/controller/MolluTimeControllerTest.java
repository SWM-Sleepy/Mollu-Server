package sleepy.mollu.server.content.mollutime.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MolluTimeControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[MOLLU 타임 조회 API 호출시] ")
    class SearchMolluTime {

        final String memberId = "memberId";
        final LocalDateTime now = LocalDateTime.now();
        final String question = "question";

        @Test
        @DisplayName("조회가 가능하다면 available과 molluTime을 응답한다.")
        void SearchMolluTime() throws Exception {
            // given
            final String accessToken = getAccessToken(memberId);
            given(molluTimeService.searchMolluTime(memberId)).willReturn(new SearchMolluTimeResponse(now, question));

            // when
            final ResultActions resultActions = get("/contents/mollu-time", accessToken);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.molluTime").isNotEmpty())
                    .andExpect(jsonPath("$.question").value(question))
                    .andDo(print());
        }

        @Test
        @DisplayName("조회가 불가능하다면 molluTime을 null로 응답한다.")
        void SearchMolluTime2() throws Exception {
            // given
            final String accessToken = getAccessToken(memberId);
            given(molluTimeService.searchMolluTime(memberId)).willReturn(new SearchMolluTimeResponse(null, null));

            // when
            final ResultActions resultActions = get("/contents/mollu-time", accessToken);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.available").doesNotExist())
                    .andExpect(jsonPath("$.molluTime").doesNotExist())
                    .andDo(print());
        }
    }
}