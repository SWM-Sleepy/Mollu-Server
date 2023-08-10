package sleepy.mollu.server.member.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.member.profile.dto.ProfileRequest;
import sleepy.mollu.server.member.profile.dto.ProfileResponse;
import sleepy.mollu.server.member.profile.service.ProfileService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

@Tag(name = "알림 설정 관련 API")
@RestController
@RequestMapping("/members/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @Operation(summary = "프로필 조회")
    @OkResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping
    public ResponseEntity<ProfileResponse> searchProfile(@Login String memberId) {

        return ResponseEntity.ok().body(profileService.searchProfile(memberId));
    }

    @Operation(summary = "프로필 변경")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PatchMapping
    public ResponseEntity<Void> updateProfile(@Login String memberId, @ModelAttribute ProfileRequest request) {

        profileService.updateProfile(memberId, request);
        return ResponseEntity.ok().build();
    }
}
