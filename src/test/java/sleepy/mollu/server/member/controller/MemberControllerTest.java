package sleepy.mollu.server.member.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.member.service.MemberService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[내 컨텐츠 조회 API 호출시] ")
    class SearchMyContents {

        @Test
        @DisplayName("쿼리 파라미터로 날짜를 전달하지 않으면 400을 던진다.")
        void SearchMyContents0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = get("/members/contents", accessToken);

            // then
            resultActions
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("200을 응답한다.")
        void SearchMyContents1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final String date = "2023-08-06";

            // when
            final ResultActions resultActions = get("/members/contents?date="+date, accessToken);

            // then
            resultActions
                    .andExpect(status().isOk());
        }
    }
}