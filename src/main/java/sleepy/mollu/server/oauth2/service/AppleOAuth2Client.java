package sleepy.mollu.server.oauth2.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppleOAuth2Client implements OAuth2Client {

    private static final String APPLE_PREFIX = "apple_";
    private final AppleIdentityTokenVerifier verifier;

    @Override
    public String getMemberId(String socialToken) {

        final Claims claims = verifier.verify(socialToken);
        final String subject = claims.getSubject();

        return APPLE_PREFIX + subject;
    }
}
