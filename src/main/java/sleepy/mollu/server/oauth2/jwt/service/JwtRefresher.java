package sleepy.mollu.server.oauth2.jwt.service;

public interface JwtRefresher {

    String refresh(String refreshToken);

    boolean canRefresh(String refreshToken);
}
