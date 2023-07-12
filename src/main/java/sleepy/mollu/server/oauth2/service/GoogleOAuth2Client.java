package sleepy.mollu.server.oauth2.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2Client implements OAuth2Client {

    private static final String GOOGLE_PREFIX = "google_";
    private final GoogleIdTokenVerifier verifier;

    @Override
    public String getMemberId(String socialToken) throws GeneralSecurityException, IOException {

        final GoogleIdToken idToken = verifier.verify(socialToken);
        final GoogleIdToken.Payload payload = idToken.getPayload();

        return GOOGLE_PREFIX + payload.getSubject();
    }
}
