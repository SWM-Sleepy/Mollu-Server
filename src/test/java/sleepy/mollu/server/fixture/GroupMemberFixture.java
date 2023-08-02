package sleepy.mollu.server.fixture;

import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.domain.GroupMemberRole;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;
import java.util.UUID;

public class GroupMemberFixture {

    public static final GroupMemberRole DEFAULT_ROLE = GroupMemberRole.MEMBER;

    public static GroupMember create(String id, Group group, Member member) {
        return GroupMember.builder()
                .id(id)
                .role(DEFAULT_ROLE)
                .group(group)
                .member(member)
                .build();
    }

    public static List<GroupMember> createAll(List<Group> groups, List<Member> members) {
        return groups.stream()
                .flatMap(group -> members.stream()
                        .map(member -> create(UUID.randomUUID().toString(), group, member)))
                .toList();
    }
}
