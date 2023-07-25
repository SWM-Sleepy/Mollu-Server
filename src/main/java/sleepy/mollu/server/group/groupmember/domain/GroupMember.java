package sleepy.mollu.server.group.groupmember.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember {

    @Id
    @Column(name = "relation_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private GroupMemberRole role;

    @Builder
    public GroupMember(@NonNull String id, @NonNull Group group, @NonNull Member member, @NonNull GroupMemberRole role) {
        this.id = id;
        setGroup(group);
        setMember(member);
        this.role = role;
    }

    private void setGroup(Group group) {
        this.group = group;
        group.addGroupMember(this);
    }

    private void setMember(Member member) {
        this.member = member;
        member.addGroupMember(this);
    }

    public boolean isSameMember(Member member) {
        return this.member.equals(member);
    }
}
