package sleepy.mollu.server.content.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.content.service.ContentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContentController.class)
class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @Test
    @DisplayName("그룹 피드 검색 API를 호출하면 200을 반환한다.")
    void ContentControllerTest() throws Exception {
        // given
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        // when
        final ResultActions result = mockMvc.perform(get("/contents/group")
                .params(params));

        // then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void ContentControllerTest2() {
        // given

        // when

        // then
    }
}