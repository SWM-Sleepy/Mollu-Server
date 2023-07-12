package sleepy.mollu.server.oauth2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import online.partyrun.jwtmanager.JwtGenerator;
import online.partyrun.jwtmanager.dto.JwtToken;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2ServiceImpl implements OAuth2Service {

    private static final String BEAN_NAME_FORMAT = "OAuth2Client";

    private final Map<String, OAuth2Client> oAuth2ClientMap;
    private final MemberRepository memberRepository;
    private final JwtGenerator jwtGenerator;

    @Override
    public SocialLoginResponse login(String type, String socialToken) throws GeneralSecurityException, IOException {

        final OAuth2Client oAuth2Client = oAuth2ClientMap.get(type + BEAN_NAME_FORMAT);
        final String memberId = oAuth2Client.getMemberId(socialToken);

        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("ID가 [" + memberId + "]인 멤버를 찾을 수 없습니다.");
        }

        final JwtToken jwtToken = jwtGenerator.generate(memberId, Set.of("member"));

        return new SocialLoginResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }
}
