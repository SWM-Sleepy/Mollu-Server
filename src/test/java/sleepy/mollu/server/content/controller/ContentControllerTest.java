package sleepy.mollu.server.content.controller;

import online.partyrun.jwtmanager.dto.JwtToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ContentControllerTest extends ControllerTest {

    @Test
    @DisplayName("그룹 피드 검색 API를 호출하면 200을 반환한다.")
    void ContentControllerTest() throws Exception {

        // given
        final String accessToken = getAccessToken();
        final String cursorId = "1";
        final String cursorEndDate = "2023-07-06T11:45:00";
        given(contentService.searchGroupFeed(anyString(), anyString(), any(LocalDateTime.class)))
                .willReturn(new GroupSearchFeedResponse("cursorId", LocalDateTime.now(), List.of()));

        // when
        final String path = "/contents/group?cursorId=" + cursorId + "&cursorEndDate=" + cursorEndDate;
        final ResultActions result = get(path, accessToken);

        // then
        result.andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("컨텐츠 삭제 API를 호출하면 204를 반환한다")
    void ContentControllerTest3() throws Exception {

        // given
        final String accessToken = getAccessToken();
        final String contentId = "contentId";

        // when
        final ResultActions resultActions = delete("/contents/" + contentId, accessToken);

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