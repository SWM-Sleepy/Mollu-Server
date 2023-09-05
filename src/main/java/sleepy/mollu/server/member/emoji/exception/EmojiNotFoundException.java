package sleepy.mollu.server.member.emoji.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class EmojiNotFoundException extends NotFoundException {
    public EmojiNotFoundException(String message) {
        super(message);
    }
}
