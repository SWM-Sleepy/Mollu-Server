package sleepy.mollu.server.group.groupmember.domain;

import sleepy.mollu.server.member.domain.Member;

import java.util.Collections;
import java.util.List;


public record GroupMembers(List<GroupMember> groupMembers) {

    public GroupMembers(List<GroupMember> groupMembers) {
        this.groupMembers = Collections.unmodifiableList(groupMembers);
    }

    public boolean hasMember(Member member) {
        return groupMembers.stream()
                .anyMatch(groupMember -> groupMember.isSameMember(member));
    }
}
