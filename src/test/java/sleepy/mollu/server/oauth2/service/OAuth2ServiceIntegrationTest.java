package sleepy.mollu.server.oauth2.service;

import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@SpringBootTest
@Transactional
class OAuth2ServiceIntegrationTest {

    @Autowired
    private OAuth2Service oAuth2Service;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("[소셜 회원가입 서비스 호출시] 멤버와 함께 알림 설정도 함께 저장된다.")
    void OAuth2ServiceIntegrationTest() throws Exception {
        // given
        final SignupRequest request = new SignupRequest("name", LocalDate.now(), "molluId");

        // when
        oAuth2Service.signup("test", "socialToken", request);

        // then
        assertThat(memberRepository.existsById("memberId")).isTrue();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public OAuth2Client testOAuth2Client() {
            return socialToken -> "memberId";
        }
    }

}