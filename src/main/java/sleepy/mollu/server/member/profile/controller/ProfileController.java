package sleepy.mollu.server.member.profile.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.member.profile.dto.ProfileResponse;
import sleepy.mollu.server.member.profile.service.ProfileService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

@RestController
@Tag(name = "알림 설정 관련 API")
@RequestMapping("/members/preference")
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
}
