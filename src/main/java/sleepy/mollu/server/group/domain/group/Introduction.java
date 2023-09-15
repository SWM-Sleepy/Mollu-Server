package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Introduction {

    private static final String DEFAULT_INTRODUCTION = "";
    private static final int INTRODUCTION_MAX_LENGTH = 20;

    @Column(name = "introduction")
    private String value = DEFAULT_INTRODUCTION;

    public Introduction(String value) {
        if (checkNull(value)) {
            return;
        }

        validateLength(value);
        this.value = value;
    }

    private boolean checkNull(String value) {
        if (value == null) {
            this.value = "";
            return true;
        }

        return false;
    }

    private void validateLength(String value) {
        if (value.length() > INTRODUCTION_MAX_LENGTH) {
            throw new IllegalArgumentException("소개글은 최대 " + INTRODUCTION_MAX_LENGTH + "자까지 입력 가능합니다.");
        }
    }
}
