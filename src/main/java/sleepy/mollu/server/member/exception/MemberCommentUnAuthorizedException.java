package sleepy.mollu.server.member.exception;

import sleepy.mollu.server.common.exception.UnAuthorizedException;

public class MemberCommentUnAuthorizedException extends UnAuthorizedException {
    public MemberCommentUnAuthorizedException(String message) {
        super(message);
    }
}
