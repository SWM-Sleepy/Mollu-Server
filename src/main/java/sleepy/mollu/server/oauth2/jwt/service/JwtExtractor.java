package sleepy.mollu.server.oauth2.jwt.service;

import sleepy.mollu.server.oauth2.jwt.dto.ExtractType;
import sleepy.mollu.server.oauth2.jwt.dto.JwtPayload;

public interface JwtExtractor {

    JwtPayload extract(String token, ExtractType type);
}
