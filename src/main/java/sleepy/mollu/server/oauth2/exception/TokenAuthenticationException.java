package sleepy.mollu.server.oauth2.exception;

import sleepy.mollu.server.common.exception.AuthenticationException;

public class TokenAuthenticationException extends AuthenticationException {

    public TokenAuthenticationException(String message) {
        super(message);
    }
}
