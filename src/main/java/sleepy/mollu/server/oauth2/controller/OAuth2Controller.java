package sleepy.mollu.server.oauth2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.oauth2.controller.annotation.RefreshToken;
import sleepy.mollu.server.oauth2.controller.annotation.SocialToken;
import sleepy.mollu.server.oauth2.dto.CheckResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;
import sleepy.mollu.server.oauth2.service.OAuth2Service;
import sleepy.mollu.server.swagger.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Tag(name = "인증 관련 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;

    @Operation(summary = "소셜 로그인")
    @CreatedResponse
    @InternalServerErrorResponse
    @PostMapping("/login/{type}")
    public ResponseEntity<TokenResponse> login(
            @PathVariable String type,
            @SocialToken String socialToken) throws GeneralSecurityException, IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(oauth2Service.login(type, socialToken));
    }

    @Operation(summary = "소셜 회원가입")
    @CreatedResponse
    @InternalServerErrorResponse
    @PostMapping("/signup/{type}")
    public ResponseEntity<TokenResponse> signup(
            @PathVariable String type,
            @SocialToken String socialToken,
            @RequestBody @Valid SignupRequest request) throws GeneralSecurityException, IOException {

        return ResponseEntity.status(HttpStatus.CREATED).body(oauth2Service.signup(type, socialToken, request));
    }

    @Operation(summary = "아이디 중복 확인")
    @OkResponse
    @BadRequestResponse
    @InternalServerErrorResponse
    @GetMapping("/check-id")
    public ResponseEntity<CheckResponse> checkId(@RequestParam String molluId) {

        return ResponseEntity.ok().body(oauth2Service.checkId(molluId));
    }

    @Operation(summary = "토큰 재발급")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ConflictResponse
    @InternalServerErrorResponse
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RefreshToken String refreshToken) {

        return ResponseEntity.status(HttpStatus.CREATED).body(oauth2Service.refresh(refreshToken));
    }

    @Operation(summary = "로그아웃")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @InternalServerErrorResponse
    @PostMapping("/logout")
    public ResponseEntity<TokenResponse> logout(@Login String memberId) {

        oauth2Service.logout(memberId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
