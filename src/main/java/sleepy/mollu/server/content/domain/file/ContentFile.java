package sleepy.mollu.server.content.domain.file;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public abstract class ContentFile {

    private final MultipartFile file;

    protected ContentFile(MultipartFile file) {

        this.file = file;
    }
}
