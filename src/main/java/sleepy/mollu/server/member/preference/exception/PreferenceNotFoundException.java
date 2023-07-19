package sleepy.mollu.server.member.preference.exception;

import sleepy.mollu.server.common.exception.NotFoundException;

public class PreferenceNotFoundException extends NotFoundException {

    public PreferenceNotFoundException(String message) {
        super(message);
    }
}
