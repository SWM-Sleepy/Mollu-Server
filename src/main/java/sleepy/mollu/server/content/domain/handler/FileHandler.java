package sleepy.mollu.server.content.domain.handler;

import sleepy.mollu.server.content.domain.file.ContentFile;

public interface FileHandler {

    /**
     * 컨텐츠를 외부에 저장한다.
     *
     * @param contentFile
     * @return 저장된 컨텐츠의 URL
     */
    String upload(ContentFile contentFile);
}
