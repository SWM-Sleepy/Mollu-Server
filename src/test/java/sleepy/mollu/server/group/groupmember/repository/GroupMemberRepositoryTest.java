package sleepy.mollu.server.group.groupmember.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sleepy.mollu.server.common.config.QueryDslConfig;
import sleepy.mollu.server.fixture.GroupFixture;
import sleepy.mollu.server.fixture.GroupMemberFixture;
import sleepy.mollu.server.fixture.MemberFixture;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class GroupMemberRepositoryTest {

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private EntityManager em;

    private Member member1, member2;
    private Group group1, group2;

    @BeforeEach
    void setUp() {
        member1 = MemberFixture.create("member1", "molluId1");
        member2 = MemberFixture.create("member2", "molluId2");
        final List<Member> members = List.of(member1, member2);

        group1 = GroupFixture.create("group1");
        group2 = GroupFixture.create("group2");
        final List<Group> groups = List.of(group1, group2);

        memberRepository.saveAll(members);
        groupRepository.saveAll(groups);
        groupMemberRepository.saveAll(GroupMemberFixture.createAll(groups, members));

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