package sleepy.mollu.server.group.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.group.controller.dto.CreateGroupResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.exception.MemberGroupUnAuthorizedException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static sleepy.mollu.server.fixture.AcceptanceFixture.그룹_생성_요청_데이터;
import static sleepy.mollu.server.fixture.MemberFixture.멤버1;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    private GroupServiceImpl groupService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private IdConstructor idConstructor;

    @Mock
    private FileHandler fileHandler;

    @Nested
    @DisplayName("[그룹원 조회 서비스 호출시] ")
    class GroupMemberResponseSearchTest {

        final String memberId = "memberId";
        final String groupId = "groupId";
        final Member member = mock(Member.class);
        final Group group = mock(Group.class);
        final GroupMember groupMember = mock(GroupMember.class);

        @Test
        @DisplayName("멤버가 없으면 NotFound 예외를 던진다.")
        void GroupMemberSearchTest() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> groupService.searchGroupMembers(memberId, groupId))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("그룹이 없으면 NotFound 예외를 던진다.")
        void GroupMemberSearchTest2() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(groupRepository.findById(groupId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> groupService.searchGroupMembers(memberId, groupId))
                    .isInstanceOf(GroupNotFoundException.class);
        }

        @Test
        @DisplayName("권한이 없으면 UnAuthorized 예외를 던진다.")
        void GroupMemberSearchTest3() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(groupRepository.findById(groupId)).willReturn(Optional.of(group));
            given(groupMemberRepository.findAllWithMemberByGroup(group)).willReturn(List.of(groupMember));
            given(groupMember.isSameMember(member)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> groupService.searchGroupMembers(memberId, groupId))
                    .isInstanceOf(MemberUnAuthorizedException.class);

        }

        @Test
        @DisplayName("그룹원을 성공적으로 조회한다.")
        void GroupMemberSearchTest4() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(groupRepository.findById(groupId)).willReturn(Optional.of(group));
            given(groupMemberRepository.findAllWithMemberByGroup(group)).willReturn(List.of(groupMember));
            given(groupMember.isSameMember(member)).willReturn(true);
            given(groupMember.getMember()).willReturn(member);

            // when
            final GroupMemberSearchResponse response = groupService.searchGroupMembers(memberId, groupId);

            // then
            assertThat(response.groupMembers()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("[소속 그룹 조회 서비스 호출시] ")
    class SearchMyGroups {

        final String memberId = "memberId";
        final Member member = mock(Member.class);
        final GroupMember groupMember = mock(GroupMember.class);
        final Group group = mock(Group.class);

        @Test
        @DisplayName("성공적으로 소속 그룹을 조회한다.")
        void SearchMyGroups() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(groupMemberRepository.findAllWithGroupByMember(member)).willReturn(List.of(groupMember));
            given(groupMember.getGroup()).willReturn(group);

            // when
            final MyGroupResponse myGroupResponse = groupService.searchMyGroups(memberId);

            // then
            assertThat(myGroupResponse.groups()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("[그룹 생성 서비스 호출시] ")
    class CreateGroup {

        final String memberId = 멤버1.getId();
        final String groupId = "groupId";
        final String groupMemberId = "groupMemberId";

        @Test
        @DisplayName("그룹을 성공적으로 생성한다.")
        void CreateGroup0() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(멤버1));
            given(idConstructor.create()).willReturn(groupId, groupMemberId);
            given(groupRepository.save(any(Group.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(groupMemberRepository.save(any(GroupMember.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            final CreateGroupResponse response = groupService.createGroup(memberId, 그룹_생성_요청_데이터);

            // then
            assertAll(
                    () -> assertThat(response.groupResponse().id()).isEqualTo(groupId),
                    () -> assertThat(response.groupMemberResponse().id()).isEqualTo(groupMemberId),
                    () -> then(groupRepository).should(times(1)).save(any(Group.class)),
                    () -> then(groupMemberRepository).should(times(1)).save(any(GroupMember.class))
            );
        }
    }

    @Nested
    @DisplayName("[그룹 코드 조회 서비스 호출시] ")
    class SearchGroupCode {

        final String memberId = "memberId";
        final String groupId = "groupId";
        final String code = "code";

        final Member member = mock(Member.class);
        final Group group = mock(Group.class);

        @Test
        @DisplayName("멤버가 그룹에 소속되어 있지 않으면, UnAuthorized 예외를 던진다.")
        void SearchGroupCode0() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(groupRepository.findById(groupId)).willReturn(Optional.of(group));
            given(groupMemberRepository.existsByMemberAndGroup(any(Member.class), any(Group.class))).willReturn(false);

            // when & then
            assertThatThrownBy(() -> groupService.searchGroupCode(memberId, groupId))
                    .isInstanceOf(MemberGroupUnAuthorizedException.class);
        }

        @Test
        @DisplayName("그룹 코드를 성공적으로 조회한다.")
        void SearchGroupCode1() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(groupRepository.findById(groupId)).willReturn(Optional.of(group));
            given(groupMemberRepository.existsByMemberAndGroup(any(Member.class), any(Group.class))).willReturn(true);
            given(group.getCode()).willReturn(code);

            // when
            final SearchGroupCodeResponse response = groupService.searchGroupCode(memberId, groupId);

            // then
            assertThat(response.code()).isEqualTo(code);
        }
    }
}