package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Question {

    @Column(name = "question")
    private String value;

    public Question(String value) {
        if (value == null) {
            this.value = "";
            return;
        }

        this.value = value;
    }
}
