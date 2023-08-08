package sleepy.mollu.server.group.groupmember.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember {

    @Id
    @Column(name = "group_member_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Enumerated(EnumType.STRING)
    private GroupMemberRole role;

    @Builder
    public GroupMember(String id, Group group, Member member, GroupMemberRole role) {
        this.id = id;
        this.group = group;
        setMember(member);
        this.role = role;
    }

    private void setMember(Member member) {
        this.member = member;
    }

    public boolean isSameMember(Member member) {
        return this.member.equals(member);
    }
}
