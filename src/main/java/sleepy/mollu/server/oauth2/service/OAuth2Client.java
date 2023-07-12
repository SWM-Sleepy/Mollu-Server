package sleepy.mollu.server.oauth2.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface OAuth2Client {

    String getMemberId(String socialToken) throws GeneralSecurityException, IOException;
}
