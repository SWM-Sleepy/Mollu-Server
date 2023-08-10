package sleepy.mollu.server.group.domain.group;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Introduction {

    @Column(name = "introduction")
    private String value;

    public Introduction(String value) {

        this.value = value;
    }
}
