package sleepy.mollu.server.oauth2.jwt.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import sleepy.mollu.server.oauth2.exception.TokenUnAuthenticatedException;
import sleepy.mollu.server.oauth2.jwt.dto.ExtractType;
import sleepy.mollu.server.oauth2.jwt.dto.JwtPayload;
import sleepy.mollu.server.oauth2.jwt.dto.JwtToken;

import java.time.Duration;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class JwtManager implements JwtExtractor, JwtGenerator, JwtRefresher {

    private static final int REFRESH_LEFT_DAYS = 5;

    TokenManager accessTokenManager;
    TokenManager refreshTokenManager;

    @Override
    public JwtPayload extract(String token, ExtractType type) {
        final TokenManager tokenManager = getTokenManager(type);

        try {
            return tokenManager.extract(token);
        } catch (MalformedJwtException e) {
            throw new TokenUnAuthenticatedException("[" + token + "] 은 잘못된 형식의 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new TokenUnAuthenticatedException("[" + token + "] 은 지원되지 않는 형식의 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new TokenUnAuthenticatedException("[" + token + "] 은 만료된 토큰입니다.");
        } catch (Exception e) {
            throw new TokenUnAuthenticatedException(e.getClass() + " [" + token + "] 은 유효하지 않은 토큰입니다.");
        }
    }

    @Override
    public JwtToken generate(String id) {
        return JwtToken.builder()
                .accessToken(accessTokenManager.generate(id))
                .refreshToken(refreshTokenManager.generate(id))
                .build();
    }

    @Override
    public JwtToken refresh(String refreshToken) {
        final JwtPayload jwtPayload = refreshTokenManager.extract(refreshToken);
        return JwtToken.builder()
                .accessToken(accessTokenManager.generate(jwtPayload.id()))
                .refreshToken(refreshTokenManager.generate(jwtPayload.id()))
                .build();
    }

    @Override
    public boolean canRefresh(String refreshToken) {
        final JwtPayload payload = refreshTokenManager.extract(refreshToken);
        final long leftExpireDays = Duration.between(LocalDateTime.now(), payload.expireAt()).toDays();
        return leftExpireDays < REFRESH_LEFT_DAYS;
    }

    private TokenManager getTokenManager(ExtractType type) {
        return type == ExtractType.ACCESS ? accessTokenManager : refreshTokenManager;
    }
}
