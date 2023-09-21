package sleepy.mollu.server.member.exception;

import sleepy.mollu.server.common.exception.UnAuthorizedException;

public class MemberContentUnAuthorizedException extends UnAuthorizedException {

    public MemberContentUnAuthorizedException(String message) {
        super(message);
    }
}
