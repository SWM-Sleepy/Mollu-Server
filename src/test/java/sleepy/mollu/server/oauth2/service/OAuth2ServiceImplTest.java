package sleepy.mollu.server.oauth2.service;

import online.partyrun.jwtmanager.JwtGenerator;
import online.partyrun.jwtmanager.dto.JwtToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OAuth2ServiceImplTest {

    private OAuth2Service oAuth2Service;

    @Mock
    private Map<String, OAuth2Client> oAuth2ClientMap;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtGenerator jwtGenerator;

    @BeforeEach
    void setUp() {
        oAuth2Service = new OAuth2ServiceImpl(oAuth2ClientMap, memberRepository, jwtGenerator);
    }

    @Nested
    @DisplayName("[소셜 로그인 서비스 호출시]")
    class SocialLoginTest {

        private final String memberId = "memberId";
        private final String type = "type";
        private final String socialToken = "socialToken";

        @Test
        @DisplayName("회원가입을 하지 않았으면 예외를 발생시킨다.")
        void OAuth2ServiceImplTest() throws GeneralSecurityException, IOException {
            // given
            OAuth2Client oAuth2Client = mock(OAuth2Client.class);
            given(oAuth2ClientMap.get(anyString())).willReturn(oAuth2Client);

            given(oAuth2Client.getMemberId(anyString())).willReturn(memberId);
            given(memberRepository.existsById(memberId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> oAuth2Service.login(type, socialToken))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("회원가입을 했으면 access 및 refresh 토큰을 발급한다.")
        void OAuth2ServiceImplTest2() throws GeneralSecurityException, IOException {
            // given
            OAuth2Client oAuth2Client = mock(OAuth2Client.class);
            given(oAuth2ClientMap.get(anyString())).willReturn(oAuth2Client);

            final String accessToken = "accessToken";
            final String refreshToken = "refreshToken";

            given(oAuth2Client.getMemberId(anyString())).willReturn(memberId);
            given(memberRepository.existsById(memberId)).willReturn(true);
            given(jwtGenerator.generate(anyString(), anySet())).willReturn(JwtToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());

            // when
            final TokenResponse tokenResponse = oAuth2Service.login(type, socialToken);

            // then
            assertAll(
                    () -> assertThat(tokenResponse.accessToken()).isEqualTo(accessToken),
                    () -> assertThat(tokenResponse.refreshToken()).isEqualTo(refreshToken)
            );
        }
    }

    @Nested
    @DisplayName("[소셜 회원가입 서비스 호출시]")
    class SocialSignupTest {

        final String type = "type";
        final String socialToken = "socialToken";
        final SignupRequest request = new SignupRequest("name", LocalDate.now(), "molluId");
        final String memberId = "memberId";


        @Test
        @DisplayName("회원가입을 했으면 예외를 던진다")
        void SocialSignupTest1() throws GeneralSecurityException, IOException {
            // given
            OAuth2Client oAuth2Client = mock(OAuth2Client.class);
            given(oAuth2ClientMap.get(anyString())).willReturn(oAuth2Client);

            given(oAuth2Client.getMemberId(anyString())).willReturn(memberId);
            given(memberRepository.existsById(memberId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> oAuth2Service.signup(type, socialToken, request))
                    .isInstanceOf(MemberBadRequestException.class);
        }

        @Test
        @DisplayName("회원가입을 하지 않았으면 회원가입 이후 토큰을 발급한다")
        void SocialSignupTest2() throws GeneralSecurityException, IOException {
            // given
            OAuth2Client oAuth2Client = mock(OAuth2Client.class);
            given(oAuth2ClientMap.get(anyString())).willReturn(oAuth2Client);

            final String accessToken = "accessToken";
            final String refreshToken = "refreshToken";

            given(oAuth2Client.getMemberId(anyString())).willReturn(memberId);
            given(memberRepository.existsById(memberId)).willReturn(false);
            given(jwtGenerator.generate(anyString(), anySet())).willReturn(JwtToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());

            // when
            final TokenResponse tokenResponse = oAuth2Service.signup(type, socialToken, request);

            // then
            assertAll(
                    () -> then(memberRepository).should(times(1)).save(any(Member.class)),
                    () -> assertThat(tokenResponse.accessToken()).isEqualTo(accessToken),
                    () -> assertThat(tokenResponse.refreshToken()).isEqualTo(refreshToken)
            );
        }
    }
}