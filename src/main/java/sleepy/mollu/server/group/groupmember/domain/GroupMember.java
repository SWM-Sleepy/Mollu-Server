package sleepy.mollu.server.group.groupmember.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
public class GroupMember {

    @Id
    @Column(name = "relation_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(EnumType.STRING)
    private GroupMemberRole role;

    @Builder
    public GroupMember(String id, Member member, Group group, GroupMemberRole role) {
        this.id = id;
        setMember(member);
        setGroup(group);
        this.role = role;
    }

    private void setMember(Member member) {
        this.member = member;
        member.addGroupMember(this);
    }

    private void setGroup(Group group) {
        this.group = group;
        group.addGroupMember(this);
    }
}
