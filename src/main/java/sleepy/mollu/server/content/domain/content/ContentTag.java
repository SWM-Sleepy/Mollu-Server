package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.content.exception.ContentTagBadRequestException;

@Embeddable
@Getter
@NoArgsConstructor
public class ContentTag {

    public static final int MAX_CONTENT_TAG_LENGTH = 10;

    @Column(name = "tag")
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
            throw new ContentTagBadRequestException("태그는 null일 수 없습니다.");
        }
    }

    private void validateBlank(String value) {
        if (value.isBlank()) {
            throw new ContentTagBadRequestException("태그는 비어있을 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_CONTENT_TAG_LENGTH) {
            throw new ContentTagBadRequestException("태그는 " + MAX_CONTENT_TAG_LENGTH + "자를 넘을 수 없습니다.");
        }
    }
}
