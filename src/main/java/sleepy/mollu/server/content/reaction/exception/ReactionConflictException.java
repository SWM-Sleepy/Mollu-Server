package sleepy.mollu.server.content.reaction.exception;

import sleepy.mollu.server.common.exception.ConflictException;

public class ReactionConflictException extends ConflictException {

    public ReactionConflictException(String message) {
        super(message);
    }
}
