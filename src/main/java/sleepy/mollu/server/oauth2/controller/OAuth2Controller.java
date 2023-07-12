package sleepy.mollu.server.oauth2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.oauth2.domain.SocialToken;
import sleepy.mollu.server.oauth2.dto.SocialLoginResponse;
import sleepy.mollu.server.oauth2.service.OAuth2Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;

    @RequestMapping("/login")
    public ResponseEntity<SocialLoginResponse> login(
            @RequestParam("type") String type,
            @SocialToken String socialToken) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(oauth2Service.login(type, socialToken));
    }
}
