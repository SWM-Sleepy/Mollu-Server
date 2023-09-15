package sleepy.mollu.server.content.domain.handler;

import com.amazonaws.services.s3.AmazonS3;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.dto.OriginThumbnail;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class S3FileHandlerTest {

    @InjectMocks
    private S3FileHandler s3FileHandler;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() {
        s3FileHandler = new S3FileHandler(amazonS3, "bucket-name");
    }

    @Test
    @DisplayName("[uploadWithThumbnail 메서드 호출시] 원본 URL과 썸네일 URL이 버킷을 제외한 키는 동일하다.")
    void S3FileHandlerTest() {
        // given
        final MultipartFile mockFile = new MockMultipartFile("photo", "file.png", "image/png", "Spring Framework".getBytes());
        final ContentFile contentFile = new ImageContentFile(mockFile, ContentType.CONTENTS);

        given(amazonS3.getUrl(anyString(), anyString())).willAnswer(invocation -> {
            final String bucketName = invocation.getArgument(0);
            final String key = invocation.getArgument(1);
            return new URL("https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/" + key);
        });

        // when
        final OriginThumbnail originThumbnail = s3FileHandler.uploadWithThumbnail(contentFile);

        // then
        final String[] originSourceSplits = originThumbnail.originSource().split("/");
        final String originSourceKey = originSourceSplits[originSourceSplits.length - 1];

        final String[] thumbnailSourceSplits = originThumbnail.thumbnailSource().split("/");
        final String thumbnailSourceKey = thumbnailSourceSplits[thumbnailSourceSplits.length - 1];

        assertThat(originSourceKey).isEqualTo(thumbnailSourceKey);
    }
}