package sleepy.mollu.server.content.controller;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.content.service.ContentService;
import sleepy.mollu.server.oauth2.config.CustomJwtConfig;

import java.util.List;
import java.util.Map;
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
        params.add("cursorId", "1");
        params.add("cursorEndDate", "2023-07-06T11:45:00");

        final HttpHeaders headers = getHeaders();

        // when
        final ResultActions result = mockMvc.perform(get("/contents/group")
                .params(params)
                .headers(headers));

        // then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("컨텐츠 삭제 API를 호출하면 204를 반환한다")
    void ContentControllerTest3() throws Exception {

        // given
        final HttpHeaders headers = getHeaders();

        final String contentId = "contentId";

        // when
        final ResultActions resultActions = mockMvc.perform(delete("/contents/" + contentId)
                .headers(headers));

        // then
        resultActions.andExpect(status().isNoContent())
                .andDo(print());
    }

    private String getAccessToken() {
        final JwtToken jwtToken = jwtGenerator.generate("memberId", Set.of("member"));
        return jwtToken.accessToken();
    }

    private HttpHeaders getHeaders() {
        final String accessToken = getAccessToken();
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        return headers;
    }

    @Nested
    @DisplayName("[컨텐츠 업로드 API 호출시] ")
    class ContentUploadTest {

        @Test
        @DisplayName("시간이 비어있으면 400을 반환한다.")
        void ContentUploadTest3() throws Exception {
            // given
            final MockMultipartFile frontContentFile = getMockMultipartFile("frontContentFile");
            final MockMultipartFile backContentFile = getMockMultipartFile("backContentFile");

            final MultiValueMap<String, String> params = getParams(List.of("location", "tag"));
            final HttpHeaders headers = getHeaders();

            // when
            final ResultActions resultActions = mockMvc.perform(multipart("/contents")
                    .file(frontContentFile)
                    .file(backContentFile)
                    .params(params)
                    .headers(headers));

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("태그를 작성하지 않아도 201을 반환한다.")
        void ContentUploadTest() throws Exception {
            // given
            final MockMultipartFile frontContentFile = getMockMultipartFile("frontContentFile");
            final MockMultipartFile backContentFile = getMockMultipartFile("backContentFile");

            final MultiValueMap<String, String> params = getParams(List.of("location", "molluDateTime", "uploadDateTime"));
            final HttpHeaders headers = getHeaders();

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

        private MockMultipartFile getMockMultipartFile(String contentFile) {
            return new MockMultipartFile(contentFile, "test_file.png",
                    "image/png", "Spring Framework".getBytes());
        }

        private MultiValueMap<String, String> getParams(List<String> keys) {
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

            final Map<String, String> map = Map.of(
                    "location", "서울 도봉구",
                    "tag", "태그",
                    "molluDateTime", "2023-07-06T11:45:00",
                    "uploadDateTime", "2023-07-06T11:45:00"
            );

            keys.forEach(key -> params.add(key, map.get(key)));

            return params;
        }
    }
}