package sleepy.mollu.server.content.exception;

import sleepy.mollu.server.common.exception.BadRequestException;

public class ContentTagBadRequestException extends BadRequestException {

    public ContentTagBadRequestException(String message) {
        super(message);
    }
}
