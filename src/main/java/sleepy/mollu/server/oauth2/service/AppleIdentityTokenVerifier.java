package sleepy.mollu.server.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sleepy.mollu.server.oauth2.exception.TokenAuthenticationException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleIdentityTokenVerifier {

    private final AppleClient appleClient;

    public Claims verify(String socialToken) {

        try {
            ApplePublicKeyResponse response = appleClient.getPublicKey();

            String headerOfIdentityToken = socialToken.substring(0, socialToken.indexOf("."));
            Map<String, String> header = new ObjectMapper()
                    .readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), StandardCharsets.UTF_8), Map.class);
            ApplePublicKeyResponse.Key key = response.getMatchedKeyBy(header.get("kid"), header.get("alg"))
                    .orElseThrow(() -> new NullPointerException("Failed get public key from apple's id server."));

            byte[] nBytes = Base64.getUrlDecoder().decode(key.getN());
            byte[] eBytes = Base64.getUrlDecoder().decode(key.getE());

            BigInteger n = new BigInteger(1, nBytes);
            BigInteger e = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance(key.getKty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(socialToken)
                    .getBody();

        } catch (MalformedJwtException e) {
            throw new TokenAuthenticationException("유효하지 않은 토큰입니다.");
        } catch (SignatureException | ExpiredJwtException e) {
            throw new TokenAuthenticationException("토큰이 만료되었습니다.");
        } catch (Exception e) {
            throw new TokenAuthenticationException(e.getClass() + " : 토큰 검증에 실패하였습니다.");
        }
    }

}
