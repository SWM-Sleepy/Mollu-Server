package sleepy.mollu.server.content.comment.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(String message) {
        super(message);
    }
}
