package sleepy.mollu.server.content.comment.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Message {

    private static final int MAX_MESSAGE_LENGTH = 200;

    private String value;

    public Message(String value) {
        validateMessage(value);
        this.value = value;
    }

    private void validateMessage(String value) {
        validateNull(value);
        validateLength(value);
    }

    private void validateNull(String value) {
        if (value == null) {
            throw new IllegalArgumentException("댓글 내용은 null이 될 수 없습니다.");
        }
    }

    private void validateLength(String value) {
        if (value.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("댓글 내용은 " + MAX_MESSAGE_LENGTH + "자를 넘을 수 없습니다.");
        }
    }
}
