package sleepy.mollu.server.oauth2.jwt.dto;

import java.time.LocalDateTime;

public record JwtPayload(String id, LocalDateTime expireAt) {
}
