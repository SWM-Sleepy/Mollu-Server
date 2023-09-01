package sleepy.mollu.server.content.domain.file;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public abstract class ContentFile {

    private final MultipartFile file;
    private final ContentType contentType;

    protected ContentFile(MultipartFile file, ContentType contentType) {

        this.file = file;
        this.contentType = contentType;
    }
}
