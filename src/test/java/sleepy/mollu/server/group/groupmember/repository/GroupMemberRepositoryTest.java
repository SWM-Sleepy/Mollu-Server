package sleepy.mollu.server.group.groupmember.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GroupMemberRepositoryTest extends RepositoryTest {

    private Member member1, member2;
    private Group group1, group2;

    @BeforeEach
    void setUp() {
        member1 = saveMember("member1", "mollua");
        member2 = saveMember("member2", "mollub");
        final List<Member> members = List.of(member1, member2);

        group1 = saveGroup("group1");
        group2 = saveGroup("group2");
        final List<Group> groups = List.of(group1, group2);

        saveGroupMembers(groups, members);

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("[findAllWithGroupByMember 호출시] 멤버가 속한 그룹멤버 목록을 그룹과 함께 조회한다.")
    void GroupMemberRepositoryTest() {
        // given & when
        System.out.println("---------");
        final List<GroupMember> groupMembers = groupMemberRepository.findAllWithGroupByMember(member1);
        System.out.println("---------");

        System.out.println("---------");
        final List<String> groupNames = groupMembers.stream()
                .map(GroupMember::getGroup)
                .map(Group::getName)
                .toList();
        System.out.println("---------");

        // then
        assertThat(groupNames).hasSize(2);
    }

    @Test
    @DisplayName("[findAllWithMemberByGroup 호출시] 그룹이 속한 그룹멤버 목록을 멤버와 함께 조회한다.")
    void GroupMemberRepositoryTest2() {
        // given & when
        System.out.println("---------");
        final List<GroupMember> groupMembers = groupMemberRepository.findAllWithMemberByGroup(group1);
        System.out.println("---------");

        System.out.println("---------");
        final List<String> memberNames = groupMembers.stream()
                .map(GroupMember::getMember)
                .map(Member::getName)
                .toList();
        System.out.println("---------");

        // then
        assertThat(memberNames).hasSize(2);
    }

}