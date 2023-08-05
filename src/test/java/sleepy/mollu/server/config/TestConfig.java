package sleepy.mollu.server.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import sleepy.mollu.server.oauth2.service.AppleOAuth2Client;
import sleepy.mollu.server.oauth2.service.GoogleOAuth2Client;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    public GoogleOAuth2Client googleOAuth2Client() throws GeneralSecurityException, IOException {
        final GoogleOAuth2Client googleOAuth2Client = mock(GoogleOAuth2Client.class);
        given(googleOAuth2Client.getMemberId(anyString())).willReturn("memberId");
        return googleOAuth2Client;
    }

    @Bean
    public AppleOAuth2Client appleOAuth2Client() {
        final AppleOAuth2Client appleOAuth2Client = mock(AppleOAuth2Client.class);
        given(appleOAuth2Client.getMemberId(anyString())).willReturn("memberId");
        return appleOAuth2Client;
    }
}
