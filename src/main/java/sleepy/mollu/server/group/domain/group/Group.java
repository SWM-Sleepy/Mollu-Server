package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.*;
import lombok.Getter;
import sleepy.mollu.server.common.domain.FileSource;

@Entity
@Getter
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
}
