package sleepy.mollu.server.oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;
import sleepy.mollu.server.oauth2.service.OAuth2Service;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;

    public ResponseEntity<SocialLoginResponse> login() {
        return ResponseEntity.ok(oauth2Service.login());
    }
}
