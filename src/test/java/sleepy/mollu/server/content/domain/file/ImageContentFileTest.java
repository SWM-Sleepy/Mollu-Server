package sleepy.mollu.server.content.domain.file;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("유효하지 않은 파일을 받으면 예외를 던진다")
    void test(String contentType) {
        // given
        final MultipartFile mockFile = new MockMultipartFile("photo", "file.png", contentType,
                "Spring Framework".getBytes());

        // when & then
        assertThatThrownBy(() -> new ImageContentFile(mockFile))
                .isInstanceOf(ImageContentFileBadRequestException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"image/png", "image/bmp", "image/jpeg"})
    @DisplayName("유효한 이미지 파일을 받으면 객체를 생성한다")
    void test2(String contentType) {
        // given
        final MultipartFile mockFile = new MockMultipartFile("photo", "file.png", contentType,
                "Spring Framework".getBytes());

        // when & then
        assertThatCode(() -> new ImageContentFile(mockFile)).doesNotThrowAnyException();
    }

}