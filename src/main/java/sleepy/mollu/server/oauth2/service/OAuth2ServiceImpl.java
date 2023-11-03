package sleepy.mollu.server.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.domain.GroupMemberRole;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Preference;
import sleepy.mollu.server.member.dto.MemberBadRequestException;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.oauth2.dto.CheckResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;
import sleepy.mollu.server.oauth2.exception.TokenUnAuthenticatedException;
import sleepy.mollu.server.oauth2.jwt.dto.ExtractType;
import sleepy.mollu.server.oauth2.jwt.dto.JwtPayload;
import sleepy.mollu.server.oauth2.jwt.dto.JwtToken;
import sleepy.mollu.server.oauth2.jwt.service.JwtExtractor;
import sleepy.mollu.server.oauth2.jwt.service.JwtGenerator;
import sleepy.mollu.server.oauth2.jwt.service.JwtRefresher;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class OAuth2ServiceImpl implements OAuth2Service {

    private static final String BEAN_NAME_FORMAT = "OAuth2Client";

    private final Map<String, OAuth2Client> oAuth2ClientMap;
    private final JwtGenerator jwtGenerator;
    private final JwtExtractor jwtExtractor;
    private final JwtRefresher jwtRefresher;

    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupRepository groupRepository;
    private final IdConstructor idConstructor;

    private static void checkRefreshTokenValid(String refreshToken, Member member) {
        if (!member.hasSameRefreshToken(refreshToken)) {
            throw new TokenUnAuthenticatedException("데이터베이스에 저장된 리프레시 토큰과 다릅니다.");
        }
    }

    @Transactional
    @Override
    public TokenResponse login(String type, String socialToken) throws GeneralSecurityException, IOException {

        final String memberId = getOAuth2MemberId(type, socialToken);
        final Member member = memberRepository.findByIdOrElseThrow(memberId);

        final JwtToken token = jwtGenerator.generate(memberId);
        member.updateRefreshToken(token.refreshToken());

        return new TokenResponse(token.accessToken(), token.refreshToken());
    }

    private String getOAuth2MemberId(String type, String socialToken) throws GeneralSecurityException, IOException {

        final OAuth2Client oAuth2Client = oAuth2ClientMap.get(type + BEAN_NAME_FORMAT);

        return oAuth2Client.getMemberId(socialToken);
    }

    @Transactional
    @Override
    public TokenResponse signup(String type, String socialToken, SignupRequest request) throws GeneralSecurityException, IOException {

        final String memberId = getOAuth2MemberId(type, socialToken);
        validateMemberExists(memberId);
        final JwtToken jwtToken = jwtGenerator.generate(memberId);
        final Member member = saveMember(memberId, request.name(), request.birthday(), request.molluId(), jwtToken.refreshToken());
        joinDefaultGroup(member);

        return new TokenResponse(jwtToken.accessToken(), jwtToken.refreshToken());
    }

    private void validateMemberExists(String memberId) {
        if (memberRepository.existsById(memberId)) {
            throw new MemberBadRequestException("ID가 [" + memberId + "]인 멤버가 이미 존재합니다.");
        }
    }

    private Member saveMember(String memberId, String name, LocalDate birthday, String molluId, String refreshToken) {
        return memberRepository.save(Member.builder()
                .id(memberId)
                .name(name)
                .birthday(birthday)
                .molluId(molluId)
                .refreshToken(refreshToken)
                .preference(getPreference())
                .build());
    }

    private Preference getPreference() {
        return Preference.builder()
                .molluAlarm(false)
                .contentAlarm(false)
                .build();
    }

    private void joinDefaultGroup(Member member) {
        final Group defaultGroup = getDefaultGroup();
        groupMemberRepository.save(GroupMember.builder()
                .id(idConstructor.create())
                .member(member)
                .group(defaultGroup)
                .role(GroupMemberRole.MEMBER)
                .build());
    }

    private Group getDefaultGroup() {
        return groupRepository.findDefaultGroup()
                .orElseThrow(() -> new GroupNotFoundException("기본 그룹이 존재하지 않습니다."));
    }

    @Override
    public CheckResponse checkId(String molluId) {

        final boolean existsMemberWithSameMolluId = memberRepository.existsByMolluId(molluId);

        return new CheckResponse(!existsMemberWithSameMolluId);
    }

    @Transactional
    @Override
    public TokenResponse refresh(String refreshToken) {
        final String memberId = getMemberIdFrom(refreshToken);
        final Member member = memberRepository.findByIdOrElseThrow(memberId);

        checkRefreshTokenValid(refreshToken, member);

        return new TokenResponse(getNewAccessToken(refreshToken), refreshToken);
    }

    private String getMemberIdFrom(String refreshToken) {
        final JwtPayload payload = jwtExtractor.extract(refreshToken, ExtractType.REFRESH);
        return payload.id();
    }

    private String getNewAccessToken(String refreshToken) {
        return jwtRefresher.refresh(refreshToken);
    }

    @Transactional
    @Override
    public void logout(String memberId) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);

        member.updatePhoneToken(null, null);
        member.updateRefreshToken(null);
    }
}
