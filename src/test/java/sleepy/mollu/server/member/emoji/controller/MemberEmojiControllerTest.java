package sleepy.mollu.server.member.emoji.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.member.emoji.controller.dto.SearchMyEmojiResponse;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberEmojiControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[내 이모티콘 등록 API 호출시] ")
    class CreateMyEmojiTest {

        @Test
        @DisplayName("emoji를 설정하지 않으면 400을 반환한다.")
        void CreateMyEmojiTest0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final String[] files = {"emojiFile"};

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/members/emojis", files, accessToken);

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("emojiFile을 설정하지 않으면 400을 반환한다.")
        void CreateMyEmojiTest1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("emoji", "emotion1");

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/members/emojis", accessToken, params);

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("파라미터를 모두 설정했으면 201을 반환한다.")
        void CreateMyEmojiTest2() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final String[] files = {"emojiFile"};
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("emoji", "emotion1");

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/members/emojis", files, accessToken, params);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Test
    @DisplayName("내 이모티콘 조회 API 호출시 200을 반환한다.")
    void MemberEmojiControllerTest() throws Exception {
        // given
        final String accessToken = getAccessToken("memberId");
        given(memberEmojiService.searchMyEmoji("memberId"))
                .willReturn(new SearchMyEmojiResponse(List.of("url1", "", "", "", "")));

        // when
        final ResultActions resultActions = get("/members/emojis", accessToken);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

    @Nested
    @DisplayName("[내 이모티콘 삭제 API 호출시] ")
    class DeleteMyEmojiTest {

        @Test
        @DisplayName("emoji를 설정하지 않으면 400을 반환한다.")
        void DeleteMyEmojiTest0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = delete("/members/emojis", accessToken);

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("파라미터를 모두 설정했으면 204를 반환한다.")
        void DeleteMyEmojiTest1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("emoji", "emotion1");

            // when
            final ResultActions resultActions = delete("/members/emojis?emoji=", accessToken);

            // then
            resultActions.andExpect(status().isNoContent())
                    .andDo(print());
        }
    }
}