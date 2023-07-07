package sleepy.mollu.server.content.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class ContentTag {

    public static final int MAX_CONTENT_TAG_LENGTH = 10;
    private String value;

    public ContentTag(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        validateNull(value);
        validateBlank(value);
        validateLength(value);
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("태그는 null일 수 없습니다.");
        }
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("태그는 비어있을 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_CONTENT_TAG_LENGTH) {
            throw new IllegalArgumentException("태그는 " + MAX_CONTENT_TAG_LENGTH + "자를 넘을 수 없습니다.");
        }
    }
}
