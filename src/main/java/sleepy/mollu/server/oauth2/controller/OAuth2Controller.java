package sleepy.mollu.server.oauth2.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.oauth2.domain.SocialToken;
import sleepy.mollu.server.oauth2.dto.TokenResponse;
import sleepy.mollu.server.oauth2.service.OAuth2Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;

    @PostMapping("/login/{type}")
    public ResponseEntity<TokenResponse> login(
            @PathVariable String type,
            @SocialToken String socialToken) throws GeneralSecurityException, IOException {

        return ResponseEntity.ok(oauth2Service.login(type, socialToken));
    }

    @PostMapping("/signup/{type}")
    public ResponseEntity<TokenResponse> signup(
            @PathVariable String type,
            @SocialToken String socialToken,
            @RequestBody @Valid SignupRequest request) throws GeneralSecurityException, IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(oauth2Service.signup(type, socialToken, request));
    }
}
