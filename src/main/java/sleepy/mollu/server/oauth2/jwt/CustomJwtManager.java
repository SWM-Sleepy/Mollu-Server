package sleepy.mollu.server.oauth2.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import online.partyrun.jwtmanager.dto.JwtPayload;
import online.partyrun.jwtmanager.manager.JwtManager;
import online.partyrun.jwtmanager.manager.TokenManager;
import sleepy.mollu.server.oauth2.exception.TokenAuthenticationException;

public class CustomJwtManager extends JwtManager {

    public CustomJwtManager(TokenManager accessTokenManager, TokenManager refreshTokenManager) {
        super(accessTokenManager, refreshTokenManager);
    }

    @Override
    public JwtPayload extract(String accessToken) {

        try {
            return super.extract(accessToken);
        } catch (MalformedJwtException e) {
            throw new TokenAuthenticationException("[" + accessToken + "] 은 잘못된 형식의 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new TokenAuthenticationException("[" + accessToken + "] 은 지원되지 않는 형식의 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new TokenAuthenticationException("[" + accessToken + "] 은 만료된 토큰입니다.");
        } catch (Exception e) {
            throw new TokenAuthenticationException(e.getClass() + " [" + accessToken + "] 은 유효하지 않은 토큰입니다.");
        }
    }
}
