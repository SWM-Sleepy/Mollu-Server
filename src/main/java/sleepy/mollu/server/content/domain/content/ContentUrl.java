package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ContentUrl {

    private String value;
}
