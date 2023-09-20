package sleepy.mollu.server.content.domain.handler;

import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.handler.dto.OriginThumbnail;

public interface FileHandler {

    /**
     * 컨텐츠를 외부에 저장한다.
     *
     * @return 저장된 컨텐츠의 URL
     */
    String upload(ContentFile contentFile);

    OriginThumbnail uploadWithThumbnail(ContentFile contentFile);
}
