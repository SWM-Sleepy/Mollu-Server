package sleepy.mollu.server.oauth2.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import online.partyrun.jwtmanager.JwtGenerator;
import online.partyrun.jwtmanager.dto.JwtToken;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.dto.MemberBadRequestException;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
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
    public TokenResponse login(String type, String socialToken) throws GeneralSecurityException, IOException {

        final String memberId = getOAuth2MemberId(type, socialToken);
        validateMemberNotExists(memberId);

        final JwtToken jwtToken = jwtGenerator.generate(memberId, Set.of("member"));

        return new TokenResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }

    private void validateMemberNotExists(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("ID가 [" + memberId + "]인 멤버를 찾을 수 없습니다.");
        }
    }

    private String getOAuth2MemberId(String type, String socialToken) throws GeneralSecurityException, IOException {

        final OAuth2Client oAuth2Client = oAuth2ClientMap.get(type + BEAN_NAME_FORMAT);

        return oAuth2Client.getMemberId(socialToken);
    }

    @Override
    public TokenResponse signup(String type, String socialToken, SignupRequest request) throws GeneralSecurityException, IOException {

        final String memberId = getOAuth2MemberId(type, socialToken);
        validateMemberExists(memberId);
        saveMember(memberId, request.name(), request.birthday(), request.molluId());

        final JwtToken jwtToken = jwtGenerator.generate(memberId, Set.of("member"));

        return new TokenResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }

    private void validateMemberExists(String memberId) {
        if (memberRepository.existsById(memberId)) {
            throw new MemberBadRequestException("ID가 [" + memberId + "]인 멤버가 이미 존재합니다.");
        }
    }

    private void saveMember(String memberId, String name, LocalDate birthday, String molluId) {

        memberRepository.save(Member.builder()
                .id(memberId)
                .name(name)
                .birthday(birthday)
                .molluId(molluId)
                .build());
    }
}
