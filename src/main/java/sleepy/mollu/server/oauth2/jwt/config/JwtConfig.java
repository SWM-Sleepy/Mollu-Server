package sleepy.mollu.server.oauth2.jwt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sleepy.mollu.server.oauth2.jwt.service.JwtManager;
import sleepy.mollu.server.oauth2.jwt.service.TokenManager;

@Configuration
public class JwtConfig {

    @Bean
    public JwtManager jwtManager(
            @Value("${jwt.access-secret-key}") String accessKey,
            @Value("${jwt.access-expire-second}") Long accessExpireSecond,
            @Value("${jwt.refresh-secret-key}") String refreshKey,
            @Value("${jwt.refresh-expire-second}") Long refreshExpireSecond) {

        return new JwtManager(
                tokenManager(accessKey, accessExpireSecond),
                tokenManager(refreshKey, refreshExpireSecond));
    }

    private TokenManager tokenManager(String key, long tokenExpireSecond) {
        return new TokenManager(key, tokenExpireSecond);
    }
}
