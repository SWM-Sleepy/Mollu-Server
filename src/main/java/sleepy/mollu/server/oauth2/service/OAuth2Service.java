package sleepy.mollu.server.oauth2.service;

import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;

public interface OAuth2Service {

    SocialLoginResponse login();
}
