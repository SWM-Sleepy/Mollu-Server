package sleepy.mollu.server.oauth2.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.util.StringUtils;
import sleepy.mollu.server.oauth2.jwt.dto.JwtPayload;

import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenManager {
    static int MIN_EXPIRE_SECONDS = 1;
    static String ID = "id";

    Key key;
    long expireSeconds;

    public TokenManager(String key, long expireSeconds) {
        validateExpireSecond(expireSeconds);

        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
        this.expireSeconds = expireSeconds;
    }

    private void validateExpireSecond(long expireSecond) {
        if (expireSecond < MIN_EXPIRE_SECONDS) {
            throw new IllegalArgumentException("expire seconds는 0보다 커야합니다.");
        }
    }

    public String generate(String id) {
        validateId(id);
        final Claims claims = getClaims(id);
        return generateToken(claims, expireSeconds);
    }

    private void validateId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException(String.format("%s는 빈 값일 수 없습니다.", ID));
        }
    }

    private Claims getClaims(String id) {
        final Claims claims = Jwts.claims();
        claims.put(ID, id);
        return claims;
    }

    private String generateToken(Claims claims, long expireSecond) {
        final LocalDateTime expireAt = LocalDateTime.now().plusSeconds(expireSecond);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public JwtPayload extract(String accessToken) {
        final Claims claims = parseClaims(accessToken);
        final String id = claims.get(ID, String.class);
        final LocalDateTime expireAt = new Timestamp(claims.getExpiration().getTime()).toLocalDateTime();
        return new JwtPayload(id, expireAt);
    }

    private Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }
}
