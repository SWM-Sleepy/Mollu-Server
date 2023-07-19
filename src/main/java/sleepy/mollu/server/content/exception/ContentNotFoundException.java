package sleepy.mollu.server.content.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class ContentNotFoundException extends NotFoundException {
    public ContentNotFoundException(String message) {
        super(message);
    }
}
