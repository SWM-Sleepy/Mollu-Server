package sleepy.mollu.server.oauth2.exception;

import sleepy.mollu.server.common.exception.UnAuthenticatedException;

public class TokenUnAuthenticatedException extends UnAuthenticatedException {

    public TokenUnAuthenticatedException(String message) {
        super(message);
    }
}
