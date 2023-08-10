package sleepy.mollu.server.content.exception;

import sleepy.mollu.server.common.exception.BadRequestException;

public class ImageContentFileBadRequestException extends BadRequestException {

    public ImageContentFileBadRequestException(String message) {
        super(message);
    }
}
