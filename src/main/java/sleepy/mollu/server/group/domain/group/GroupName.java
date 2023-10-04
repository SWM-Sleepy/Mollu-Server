package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class GroupName {

    private static final int GROUP_NAME_MAX_LENGTH = 15;

    @Column(name = "group_name")
    private String value;

    public GroupName(String value) {

        validateNull(value);
        validateEmpty(value);
        validateLength(value);
        this.value = value;
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("[GroupName]은 null일 수 없습니다.");
        }
    }

    private void validateEmpty(String value) {
        if (value.isEmpty()) {
            throw new IllegalArgumentException("[GroupName]은 빈 값일 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() > GROUP_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException("[GroupName]은 최대 " + GROUP_NAME_MAX_LENGTH + "자까지 입력 가능합니다.");
        }
    }
}
