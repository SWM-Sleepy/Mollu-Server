package sleepy.mollu.server.oauth2.jwt.service;

import sleepy.mollu.server.oauth2.jwt.dto.JwtToken;

public interface JwtRefresher {

    JwtToken refresh(String refreshToken);
}
