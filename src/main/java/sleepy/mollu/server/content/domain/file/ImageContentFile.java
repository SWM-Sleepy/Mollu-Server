package sleepy.mollu.server.content.domain.file;

import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.exception.ImageContentFileBadRequestException;

public class ImageContentFile extends ContentFile {

    private static final String VALID_START_TYPE = "image";
    private static final String INVALID_END_TYPE = "gif";

    public ImageContentFile(MultipartFile file) {

        super(file);
        validateType(file);
    }

    private void validateType(MultipartFile file) {
        final String contentType = file.getContentType();

        validateTypeNull(contentType);
        validateImage(contentType);
        validateGIF(contentType);
    }

    private void validateTypeNull(String contentType) {
        if (contentType == null) {
            throw new ImageContentFileBadRequestException("파일 타입은 null일 수 없습니다.");
        }
    }

    private void validateImage(String contentType) {
        if (!contentType.startsWith(VALID_START_TYPE)) {
            throw new ImageContentFileBadRequestException("파일이 이미지 형식이 아닙니다.");
        }
    }

    private void validateGIF(String contentType) {
        if (contentType.endsWith(INVALID_END_TYPE)) {
            throw new ImageContentFileBadRequestException("gif 파일은 올바른 형식이 아닙니다.");
        }
    }
}
