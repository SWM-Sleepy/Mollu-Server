package sleepy.mollu.server.content.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Location {

    @Column(name = "location")
    private String value;
}
