package sleepy.mollu.server.group.groupmember.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class GroupMember extends BaseEntity {

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
        this.member = member;
        this.role = role;
    }

    public String getGroupId() {
        return this.group.getId();
    }

    public String getMemberId() {
        return this.member.getId();
    }

    public boolean isSameMember(Member member) {
        return this.member.equals(member);
    }
}
