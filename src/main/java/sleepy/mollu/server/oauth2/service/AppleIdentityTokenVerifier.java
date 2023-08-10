package sleepy.mollu.server.oauth2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sleepy.mollu.server.oauth2.exception.TokenUnAuthenticatedException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleIdentityTokenVerifier {

    private final AppleClient appleClient;

    public Claims verify(String socialToken) {

        try {
            Map<String, String> header = extractJwtHeader(socialToken);
            final ApplePublicKeyResponse response = appleClient.getPublicKey();
            ApplePublicKeyResponse.Key key = response.getMatchedPublicKeyBy(header.get("kid"), header.get("alg"));

            final PublicKey rsaPublicKey = getAppleRSAPublicKey(key.getN(), key.getE(), key.getKty());

            return validateJwt(socialToken, rsaPublicKey);

        } catch (MalformedJwtException e) {
            throw new TokenUnAuthenticatedException("유효하지 않은 토큰입니다.");
        } catch (SignatureException | ExpiredJwtException e) {
            throw new TokenUnAuthenticatedException("토큰이 만료되었습니다.");
        } catch (Exception e) {
            throw new TokenUnAuthenticatedException(e.getClass() + " : 토큰 검증에 실패하였습니다.");
        }
    }

    private Map<String, String> extractJwtHeader(String socialToken) throws JsonProcessingException {
        final String jwtTokenHeader = socialToken.substring(0, socialToken.indexOf("."));
        final byte[] decodedHeader = Base64.getDecoder().decode(jwtTokenHeader);
        final String decodedHeaderJson = new String(decodedHeader, StandardCharsets.UTF_8);

        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(decodedHeaderJson, new TypeReference<Map<String, String>>() {
        });
    }

    private PublicKey getAppleRSAPublicKey(String encodedN, String encodedE, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        final byte[] decodedN = Base64.getUrlDecoder().decode(encodedN);
        final byte[] decodedE = Base64.getUrlDecoder().decode(encodedE);

        final BigInteger n = new BigInteger(1, decodedN);
        final BigInteger e = new BigInteger(1, decodedE);

        final RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        final KeyFactory keyFactory = KeyFactory.getInstance(algorithm);

        return keyFactory.generatePublic(publicKeySpec);
    }


    private Claims validateJwt(String socialToken, PublicKey rsaPublicKey) {
        return Jwts.parserBuilder()
                .setSigningKey(rsaPublicKey)
                .build()
                .parseClaimsJws(socialToken)
                .getBody();
    }
}
