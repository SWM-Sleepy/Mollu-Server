package sleepy.mollu.server.oauth2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    private final Map<String, OAuth2Client> oAuth2ClientMap;

    @Override
    public SocialLoginResponse login(String type, String socialToken) throws GeneralSecurityException, IOException {

        final OAuth2Client oAuth2Client = oAuth2ClientMap.get(type);
        final String memberId = oAuth2Client.getMemberId(socialToken);

        // TODO: memberId로 멤버 테이블을 조회한다.
        // TODO: 존재하지 않으면 not found를 반환한다.

        // TODO: 이미 존재하는 멤버이면 access 토큰과 refresh 토큰을 반환한다.


        return null;
    }
}
