package sleepy.mollu.server.group.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.ControllerTest;
import sleepy.mollu.server.group.controller.dto.CreateGroupRequest;
import sleepy.mollu.server.group.controller.dto.CreateGroupResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sleepy.mollu.server.fixture.AcceptanceFixture.초대_코드로_그룹_참여_요청_데이터;

class GroupControllerTest extends ControllerTest {

    @Nested
    @DisplayName("[그룹 생성 API 호출시] ")
    class GroupControllerTest0 {

        final String memberId = "memberId";
        final String groupId = "groupId";

        final CreateGroupResponse response = mock(CreateGroupResponse.class);
        final CreateGroupResponse.GroupResponse groupResponse = mock(CreateGroupResponse.GroupResponse.class);

        @Test
        @DisplayName("그룹 이름이 없으면 400을 응답한다.")
        void GroupControllerTest0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("introduction", "groupIntroduction");

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/groups", accessToken, params);


            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("파라미터가 모두 설정되어 있으면 그룹을 생성한다.")
        void GroupControllerTest1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final String[] files = {"imageFile"};
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("name", "groupName");
            params.add("introduction", "groupIntroduction");

            given(groupService.createGroup(eq(memberId), any(CreateGroupRequest.class))).willReturn(response);
            given(response.groupResponse()).willReturn(groupResponse);
            given(groupResponse.id()).willReturn(groupId);

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/groups", files, accessToken, params);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @DisplayName("그룹 소개를 작성하지 않아도 그룹 생성이 가능하다.")
        void GroupControllerTest3() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("name", "groupName");

            given(groupService.createGroup(eq(memberId), any(CreateGroupRequest.class))).willReturn(response);
            given(response.groupResponse()).willReturn(groupResponse);
            given(groupResponse.id()).willReturn(groupId);

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/groups", accessToken, params);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }

        @Test
        @DisplayName("프로필 이미지를 보내지 않아도 정상적으로 그룹 생성이 가능하다.")
        void GroupControllerTest2() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");
            final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("name", "groupName");
            params.add("introduction", "groupIntroduction");

            given(groupService.createGroup(eq(memberId), any(CreateGroupRequest.class))).willReturn(response);
            given(response.groupResponse()).willReturn(groupResponse);
            given(groupResponse.id()).willReturn(groupId);

            // when
            final ResultActions resultActions = multipart(HttpMethod.POST, "/groups", accessToken, params);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("[초대 코드로 그룹 참여 API 호출시] ")
    class JoinGroupByCode {

        @Test
        @DisplayName("요청 바디에 초대 코드가 없으면, 400을 응답한다.")
        void JoinGroupByCode0() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/groups/code", accessToken);

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andDo(print());
        }

        @Test
        @DisplayName("요청이 유효하다면, 201을 응답한다.")
        void JoinGroupByCode1() throws Exception {
            // given
            final String accessToken = getAccessToken("memberId");

            // when
            final ResultActions resultActions = post("/groups/code", accessToken, 초대_코드로_그룹_참여_요청_데이터);

            // then
            resultActions.andExpect(status().isCreated())
                    .andDo(print());
        }
    }
}