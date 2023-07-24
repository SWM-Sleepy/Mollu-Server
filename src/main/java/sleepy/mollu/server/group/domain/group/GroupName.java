package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class GroupName {

    @Column(name = "group_name")
    private String value;

    public GroupName(String value) {

        validateNull(value);
        this.value = value;
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Group name은 null일 수 없습니다.");
        }
    }
}
