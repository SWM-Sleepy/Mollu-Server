package sleepy.mollu.server.oauth2.service;

import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.oauth2.dto.CheckResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface OAuth2Service {

    TokenResponse login(String type, String socialToken) throws GeneralSecurityException, IOException;

    TokenResponse signup(String type, String socialToken, SignupRequest request) throws GeneralSecurityException, IOException;

    CheckResponse checkId(String molluId);

    TokenResponse refresh(String refreshToken);

    void logout(String memberId);
}
