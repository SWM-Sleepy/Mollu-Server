package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.common.domain.FileSource;

@Entity
@Table(name = "`group`")
@Getter
@NoArgsConstructor
public class Group extends BaseEntity {

    @Id
    @Column(name = "group_id")
    private String id;

    @Embedded
    private GroupName name;

    @Embedded
    private Introduction introduction;

    @Embedded
    private Code code;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "group_profile_source"))
    private FileSource groupProfileSource;

    @Builder
    public Group(String id, String name, String introduction, Code code, String groupProfileSource) {
        this.id = id;
        this.name = new GroupName(name);
        this.introduction = new Introduction(introduction);
        this.code = code;
        this.groupProfileSource = new FileSource(groupProfileSource);
    }

    public String getName() {
        return this.name.getValue();
    }

    public String getIntroduction() {
        return this.introduction.getValue();
    }

    public String getCode() {
        return this.code.getValue();
    }

    public String getGroupProfileSource() {
        return this.groupProfileSource.getValue();
    }
}
