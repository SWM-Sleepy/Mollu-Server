package sleepy.mollu.server.member.preference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.dto.PreferenceResponse;
import sleepy.mollu.server.member.preference.service.PreferenceService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

@Tag(name = "알림 설정 관련 API")
@RestController
@RequestMapping("/members/preference")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @Operation(summary = "알림 설정 변경")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PutMapping
    public ResponseEntity<Void> updatePreference(@Login String memberId, @RequestBody @Valid PreferenceRequest request) {

        preferenceService.updatePreference(memberId, request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림 설정 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping
    public ResponseEntity<PreferenceResponse> searchPreference(@Login String memberId) {

        return ResponseEntity.ok().body(preferenceService.searchPreference(memberId));
    }
}
