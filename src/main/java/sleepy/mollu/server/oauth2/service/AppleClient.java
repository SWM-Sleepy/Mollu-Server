package sleepy.mollu.server.oauth2.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppleClient {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String APPLE_PUBLIC_KEY_URL = "https://appleid.apple.com/auth/keys";

    public ApplePublicKeyResponse getPublicKey() {
        return REST_TEMPLATE.getForObject(APPLE_PUBLIC_KEY_URL, ApplePublicKeyResponse.class);
    }
}
