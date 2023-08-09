package sleepy.mollu.server.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MolluId {

    public static final String MOLLU_ID_PATTERN = "^(?=.*[a-z])[a-z0-9_]+$";
    private static final int MIN_MOLLU_ID_LENGTH = 3;
    private static final int MAX_MOLLU_ID_LENGTH = 20;

    @Column(name = "mollu_id", unique = true)
    private String value;

    public MolluId(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateNullOrEmpty(value);
        validateLength(value);
        validatePattern(value);
    }

    private void validateNullOrEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MolluId는 null이거나 공백일 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_MOLLU_ID_LENGTH || value.length() < MIN_MOLLU_ID_LENGTH) {
            throw new IllegalArgumentException("MolluId는 " + MIN_MOLLU_ID_LENGTH + "자 이상 " + MAX_MOLLU_ID_LENGTH + "자 이하여야 합니다.");
        }
    }

    private void validatePattern(String value) {
        if (!value.matches(MOLLU_ID_PATTERN)) {
            throw new IllegalArgumentException("MolluId는 영어 소문자만 가능합니다.");
        }
    }
}