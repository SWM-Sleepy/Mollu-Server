package sleepy.mollu.server.member.preference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.service.PreferenceService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.BadRequestResponse;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

@Tag(name = "알림 설정 관련 API")
@RestController
@RequestMapping("/members/preference")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    @Operation(summary = "알림 설정 변경")
    @OkResponse
    @BadRequestResponse
    @InternalServerErrorResponse
    @PutMapping
    public ResponseEntity<Void> updatePreference(@Login String memberId, @RequestBody @Valid PreferenceRequest request) {

        preferenceService.updatePreference(memberId, request);

        return ResponseEntity.ok().build();
    }
}
