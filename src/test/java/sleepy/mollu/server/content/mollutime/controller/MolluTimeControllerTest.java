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

        @Test
        @DisplayName("조회가 가능하다면 available과 molluTime을 응답한다.")
        void SearchMolluTime() throws Exception {
            // given
            final String accessToken = getAccessToken(memberId);
            given(molluTimeService.searchMolluTime(memberId)).willReturn(new SearchMolluTimeResponse(true, now));

            // when
            final ResultActions resultActions = get("/contents/mollu-time", accessToken);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.available").value(true))
                    .andExpect(jsonPath("$.molluTime").isNotEmpty())
                    .andDo(print());
        }

        @Test
        @DisplayName("조회가 불가능하다면 molluTime을 null로 응답한다.")
        void SearchMolluTime2() throws Exception {
            // given
            final String accessToken = getAccessToken(memberId);
            given(molluTimeService.searchMolluTime(memberId)).willReturn(new SearchMolluTimeResponse(false, null));

            // when
            final ResultActions resultActions = get("/contents/mollu-time", accessToken);

            // then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.available").value(false))
                    .andExpect(jsonPath("$.molluTime").doesNotExist())
                    .andDo(print());
        }
    }
}