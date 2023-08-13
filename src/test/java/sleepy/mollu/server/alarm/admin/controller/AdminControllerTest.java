package sleepy.mollu.server.alarm.admin.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.ControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[어드민 페이지 접근시] ")
    class AdminPageAccessTest {

        @Test
        @DisplayName("세션이 없으면 로그인 페이지로 리다이렉트된다.")
        void AdminPageAccessTest0() throws Exception {
            // given & when
            final ResultActions resultActions = get("/admin/alarm/mollu-range");

            // then
            resultActions.andExpect(status().is3xxRedirection())
                    .andDo(print());
        }

        @Test
        @DisplayName("세션이 있으면 어드민 페이지를 조회할 수 있다.")
        void AdminPageAccessTest1() throws Exception {
            // given
            final MockHttpSession session = new MockHttpSession();
            session.setAttribute("adminSession", "adminSession");

            // when
            final ResultActions resultActions = get("/admin/alarm/mollu-alarm", session);

            // then
            resultActions.andExpect(status().isOk())
                    .andDo(print());
        }
    }
}