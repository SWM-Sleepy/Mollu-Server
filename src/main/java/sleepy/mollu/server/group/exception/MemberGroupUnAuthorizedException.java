package sleepy.mollu.server.group.exception;

import sleepy.mollu.server.common.exception.UnAuthorizedException;

public class MemberGroupUnAuthorizedException extends UnAuthorizedException {

    public MemberGroupUnAuthorizedException(String message) {
        super(message);
    }
}
