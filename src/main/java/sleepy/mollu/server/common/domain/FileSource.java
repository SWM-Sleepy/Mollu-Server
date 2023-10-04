package sleepy.mollu.server.common.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class FileSource {

    private static final String DEFAULT_FILE_SOURCE = "";

    private String value;

    public FileSource(String value) {
        if (value == null) {
            this.value = DEFAULT_FILE_SOURCE;
            return;
        }

        this.value = value;
    }
}
