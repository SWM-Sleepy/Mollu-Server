package sleepy.mollu.server.oauth2.service;

import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface OAuth2Service {

    SocialLoginResponse login(String type, String socialToken) throws GeneralSecurityException, IOException;
}
