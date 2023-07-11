package sleepy.mollu.server.oauth2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;

import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    private final Map<String, OAuth2Client> oAuth2ClientMap;

    @Override
    public SocialLoginResponse login() {
        return null;
    }
}
