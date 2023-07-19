package sleepy.mollu.server.content.controller;

import online.partyrun.jwtmanager.JwtExtractor;
import online.partyrun.jwtmanager.JwtGenerator;
import online.partyrun.jwtmanager.dto.JwtToken;
import online.partyrun.jwtmanager.manager.JwtManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.content.service.ContentService;
import sleepy.mollu.server.oauth2.config.CustomJwtConfig;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContentController.class)
@Import(CustomJwtConfig.class)
class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @Autowired
    private JwtGenerator jwtGenerator;

    @Test
    @DisplayName("그룹 피드 검색 API를 호출하면 200을 반환한다.")
    void ContentControllerTest() throws Exception {

        // given
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
        final String accessToken = jwtToken.accessToken();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // when
        final ResultActions result = mockMvc.perform(get("/contents/group")
                .params(params)
                .headers(headers));

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

        final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
        final String accessToken = jwtToken.accessToken();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        // when
        final ResultActions resultActions = mockMvc.perform(multipart("/contents")
                .file(frontContentFile)
                .file(backContentFile)
                .params(params)
                .headers(headers));

        // then
        resultActions.andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("컨텐츠 삭제 API를 호출하면 204를 반환한다")
    void ContentControllerTest3() throws Exception {

        // given
        final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
        final String accessToken = jwtToken.accessToken();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        final String contentId = "contentId";

        // when
        final ResultActions resultActions = mockMvc.perform(delete("/contents/" + contentId)
                .headers(headers));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print());
    }
}