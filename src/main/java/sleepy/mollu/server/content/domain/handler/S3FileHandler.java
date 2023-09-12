package sleepy.mollu.server.content.domain.handler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.handler.dto.OriginThumbnail;
import sleepy.mollu.server.content.exception.FileHandlerServerException;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3FileHandler implements FileHandler {

    private static final String THUMBNAIL_URL = "thumbnail/";

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String upload(ContentFile contentFile) {
        final MultipartFile file = contentFile.getFile();
        final String key = getFileKey(contentFile, UploadType.ORIGIN);
        final ObjectMetadata objectMetadata = getObjectMetadata(file);

        return uploadFile(file, key, objectMetadata);
    }

    private String getFileKey(ContentFile contentFile, UploadType uploadType) {
        final String directory = contentFile.getContentType().toString().toLowerCase() + "/";
        final String fileName = UUID.randomUUID().toString() + '_' + contentFile.getFile().getOriginalFilename();

        if (uploadType == UploadType.ORIGIN) {
            return directory + fileName;

        }
        return directory + THUMBNAIL_URL + fileName;
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());
        return objectMetadata;
    }

    private String uploadFile(MultipartFile file, String key, ObjectMetadata objectMetadata) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucketName, key, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            throw new FileHandlerServerException("파일 업로드에 실패하였습니다.");
        }

        return getUrlString(key);
    }

    private String getUrlString(String key) {
        return amazonS3.getUrl(bucketName, key).toString();
    }

    @Override
    public OriginThumbnail uploadWithThumbnail(ContentFile contentFile) {
        final MultipartFile file = contentFile.getFile();
        final String originKey = getFileKey(contentFile, UploadType.ORIGIN);
        final String thumbnailKey = getFileKey(contentFile, UploadType.THUMBNAIL);
        final ObjectMetadata objectMetadata = getObjectMetadata(file);

        final String originSource = uploadFile(file, originKey, objectMetadata);
        final String thumbnailSource = getUrlString(thumbnailKey);

        return new OriginThumbnail(originSource, thumbnailSource);
    }
}
