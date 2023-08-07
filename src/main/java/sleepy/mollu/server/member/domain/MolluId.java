package sleepy.mollu.server.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MolluId {

    public static final String SMALL_LETTER_ENGLISH_PATTERN = "^[a-z]*$";
    private static final int MAX_MOLLU_ID_LENGTH = 20;

    @Column(name = "mollu_id", unique = true)
    private String value;

    public MolluId(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateNullOrEmpty(value);
        validateEnglish(value);
        validateLength(value);
    }

    private void validateNullOrEmpty(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MolluId는 null이거나 공백일 수 없습니다.");
        }
    }

    private void validateEnglish(String value) {
        if (!value.matches(SMALL_LETTER_ENGLISH_PATTERN)) {
            throw new IllegalArgumentException("MolluId는 영어만 가능합니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_MOLLU_ID_LENGTH) {
            throw new IllegalArgumentException("MolluId는 " + MAX_MOLLU_ID_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}