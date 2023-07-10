package sleepy.mollu.server.content.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.content.service.ContentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    @DisplayName("컨텐츠 업로드 API를 호출하면 201을 반환한다.")
    void ContentControllerTest2() throws Exception {
        // given
        final MockMultipartFile frontContentFile = new MockMultipartFile("frontContentFile", "test_file.png",
                "image/png", "Spring Framework".getBytes());
        final MockMultipartFile backContentFile = new MockMultipartFile("backContentFile", "test_file.png",
                "image/png", "Spring Framework".getBytes());

        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("location", "서울 도봉구");
        params.add("groupIds", "1, 2, 3");
        params.add("tag", "태그");

        // when
        final ResultActions resultActions = mockMvc.perform(multipart("/contents")
                .file(frontContentFile)
                .file(backContentFile)
                .params(params));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }
}