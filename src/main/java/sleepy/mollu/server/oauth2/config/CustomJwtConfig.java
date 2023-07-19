package sleepy.mollu.server.oauth2.config;

import online.partyrun.jwtmanager.manager.JwtManager;
import online.partyrun.jwtmanager.manager.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sleepy.mollu.server.oauth2.jwt.CustomJwtManager;

@Configuration
public class CustomJwtConfig {

    @Bean
    public JwtManager jwtManager(
            @Value("${jwt.access-secret-key}") String accessKey,
            @Value("${jwt.access-expire-second}") Long accessExpireSecond,
            @Value("${jwt.refresh-secret-key}") String refreshKey,
            @Value("${jwt.refresh-expire-second}") Long refreshExpireSecond) {

        return new CustomJwtManager(
                tokenManager(accessKey, accessExpireSecond),
                tokenManager(refreshKey, refreshExpireSecond));
    }

    private TokenManager tokenManager(String key, long tokenExpireSecond) {
        return new TokenManager(key, tokenExpireSecond);
    }
}
