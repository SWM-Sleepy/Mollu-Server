package sleepy.mollu.server.oauth2.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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