package sleepy.mollu.server.member.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class MolluId {

    private static final int MAX_MOLLU_ID_LENGTH = 20;

    @Column(name = "mollu_id")
    private String value;

    public MolluId(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("MolluId는 null이거나 공백일 수 없습니다.");
        }

        if(value.length() > MAX_MOLLU_ID_LENGTH) {
            throw new IllegalArgumentException("MolluId는 " + MAX_MOLLU_ID_LENGTH + "자를 초과할 수 없습니다.");
        }
    }
}
