package sleepy.mollu.server.content.report.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.partyrun.jwtmanager.JwtGenerator;
import online.partyrun.jwtmanager.dto.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import sleepy.mollu.server.content.report.service.ReportService;
import sleepy.mollu.server.oauth2.config.CustomJwtConfig;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContentReportController.class)
@Import(CustomJwtConfig.class)
class ContentReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Nested
    @DisplayName("[컨텐츠 신고 API 호출시] ")
    class ReportContentTest {

        @Test
        @DisplayName("신고 사유를 적지 않아도 201을 응답한다.")
        void reportContentTest() throws Exception {
            // given
            final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
            final String accessToken = jwtToken.accessToken();
            final HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);

            final String contentId = "contentId";

            // when
            final ResultActions result = mockMvc.perform(post("/contents/" + contentId + "/report")
                    .headers(headers));

            // then
            result.andExpect(status().isCreated())
                    .andDo(print());
        }
    }
}