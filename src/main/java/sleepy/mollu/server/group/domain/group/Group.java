package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.FileSource;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Group {

    @Id
    @Column(name = "group_id")
    private String id;

    @Embedded
    private GroupName name;

    @Embedded
    private Introduction introduction;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "group_profile_source"))
    private FileSource groupProfileSource;

    @OneToMany(mappedBy = "group")
    private List<GroupMember> groupMembers = new ArrayList<>();

    @Builder
    public Group(String id, String name, String introduction, String groupProfileSource) {
        this.id = id;
        this.name = new GroupName(name);
        this.introduction = new Introduction(introduction);
        this.groupProfileSource = new FileSource(groupProfileSource);
    }

    public void addGroupMember(GroupMember groupMember) {
        this.groupMembers.add(groupMember);
    }

    public boolean hasMember(Member member) {
        return this.groupMembers.stream()
                .anyMatch(groupMember -> groupMember.isSameMember(member));
    }
}
