package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class GroupName {

    private String value;
}
