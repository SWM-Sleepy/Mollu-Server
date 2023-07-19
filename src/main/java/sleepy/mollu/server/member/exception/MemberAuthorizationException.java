package sleepy.mollu.server.member.exception;

import sleepy.mollu.server.common.exception.AuthorizationException;

public class MemberAuthorizationException extends AuthorizationException {

    public MemberAuthorizationException(String message) {
        super(message);
    }
}
