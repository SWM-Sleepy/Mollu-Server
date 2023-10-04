package sleepy.mollu.server.group.exception;

import sleepy.mollu.server.common.exception.BadRequestException;

public class GroupBadRequestException extends BadRequestException {

    public GroupBadRequestException(String message) {
        super(message);
    }
}
