package sleepy.mollu.server.member.exception;

import sleepy.mollu.server.common.exception.UnAuthorizedException;

public class MemberUnAuthorizedException extends UnAuthorizedException {

    public MemberUnAuthorizedException(String message) {
        super(message);
    }
}
