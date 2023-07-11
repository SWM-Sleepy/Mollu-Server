package sleepy.mollu.server.oauth2.service;

import org.springframework.stereotype.Service;

@Service
public class GoogleOAuth2Client implements OAuth2Client {

    @Override
    public String getMemberId(String socialToken) {
        return null;
    }
}
