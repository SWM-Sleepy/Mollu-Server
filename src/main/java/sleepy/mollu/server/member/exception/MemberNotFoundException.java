package sleepy.mollu.server.member.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(String message) {
        super(message);
    }
}
