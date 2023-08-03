package sleepy.mollu.server.oauth2.jwt.dto;

import lombok.Builder;

@Builder
public record JwtToken(String accessToken, String refreshToken) {
}
