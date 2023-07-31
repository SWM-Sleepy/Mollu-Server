package sleepy.mollu.server.group.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private GroupService groupService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;


    @BeforeEach
    void setUp() {
        groupService = new GroupServiceImpl(memberRepository, groupRepository, groupMemberRepository);
    }

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
            given(groupMember.getMember()).willReturn(member);;

            // when
            final GroupMemberSearchResponse response = groupService.searchGroupMembers(memberId, groupId);

            // then
            assertThat(response.groupMemberResponses()).hasSize(1);
        }
    }

}