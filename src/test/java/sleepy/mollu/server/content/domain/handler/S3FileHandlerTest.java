package sleepy.mollu.server.content.domain.handler;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.common.config.AwsConfig;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest(classes = {S3FileHandler.class, AwsConfig.class})
@ExtendWith(MockitoExtension.class)
@Disabled
class S3FileHandlerTest {

    @Autowired
    private FileHandler fileHandler;

    @Test
    @DisplayName("컨텐츠 파일을 성공적으로 업로드한다")
    void S3FileHandlerTest2() {
        // given
        final MultipartFile file = new MockMultipartFile("photo", "test_file.png", "image/png",
                "Spring Framework".getBytes());
        final ContentFile contentFile = new ImageContentFile(file, ContentType.CONTENTS);

        assertThatCode(() -> fileHandler.uploadWithThumbnail(contentFile)).doesNotThrowAnyException();
    }

}