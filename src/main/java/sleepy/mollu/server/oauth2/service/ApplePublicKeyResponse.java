package sleepy.mollu.server.oauth2.service;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;

@Getter
@NoArgsConstructor
public class ApplePublicKeyResponse {

    private List<Key> keys;

    public Key getMatchedPublicKeyBy(String kid, String alg) {
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid) && key.getAlg().equals(alg))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("매칭되는 키를 찾을 수 없습니다."));
    }

    @Getter
    public static class Key {
        private String kty;
        private String kid;
        private String use;
        private String alg;
        private String n;
        private String e;
    }
}
