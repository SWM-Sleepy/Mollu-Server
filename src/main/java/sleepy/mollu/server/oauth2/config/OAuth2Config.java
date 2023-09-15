package sleepy.mollu.server.oauth2.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class OAuth2Config {

    @Value("${oauth2.google.clientId.ios}")
    private final String iosClientId;

    @Value("${oauth2.google.clientId.android}")
    private final String androidClientId;

    @Value("${oauth2.google.clientId.web}")
    private final String webClientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(iosClientId, androidClientId, webClientId))
                .build();
    }
}
