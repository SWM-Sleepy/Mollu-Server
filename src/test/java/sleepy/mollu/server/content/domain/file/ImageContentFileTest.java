package sleepy.mollu.server.content.domain.file;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.exception.ImageContentFileBadRequestException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ImageContentFileTest {

    @ParameterizedTest
    @ValueSource(strings = {"text/plain", "text/html", "application/octet-stream", "image/gif"})
    @NullAndEmptySource
    void 유효하지_않은_파일을_받으면_예외를_던진다(String contentType) {
        // given
        final MultipartFile mockFile = new MockMultipartFile("photo", "file.png", contentType,
                "Spring Framework".getBytes());

        // when & then
        assertThatThrownBy(() -> new ImageContentFile(mockFile))
                .isInstanceOf(ImageContentFileBadRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"image/png", "image/bmp", "image/jpeg"})
    void 이미지_파일을_받으면_객체를_생성한다(String contentType) {
        // given
        final MultipartFile mockFile = new MockMultipartFile("photo", "file.png", contentType,
                "Spring Framework".getBytes());

        // when & then
        assertThatCode(() -> new ImageContentFile(mockFile)).doesNotThrowAnyException();
    }

}