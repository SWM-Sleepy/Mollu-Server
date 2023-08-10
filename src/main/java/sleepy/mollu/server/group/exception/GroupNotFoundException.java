package sleepy.mollu.server.group.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class GroupNotFoundException extends NotFoundException {

    public GroupNotFoundException(String message) {
        super(message);
    }
}
