package sleepy.mollu.server.content.domain.handler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.exception.FileHandlerServerException;

import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3FileHandler implements FileHandler {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(ContentFile contentFile) {
        MultipartFile file = contentFile.getFile();
        final String key = UUID.randomUUID().toString() + '_' + file.getOriginalFilename();

        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new FileHandlerServerException("파일 업로드에 실패하였습니다.");
        }

        return amazonS3.getUrl(bucketName, key).toString();
    }
}
