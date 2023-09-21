package sleepy.mollu.server.member.exception;

import sleepy.mollu.server.common.exception.UnAuthorizedException;

public class MemberReactionUnAuthorizedException extends UnAuthorizedException {
    public MemberReactionUnAuthorizedException(String message) {
        super(message);
    }
}
