package sleepy.mollu.server.content.mollutime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.content.mollutime.service.MolluTimeService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.NotFoundResponse;
import sleepy.mollu.server.swagger.OkResponse;
import sleepy.mollu.server.swagger.UnAuthorizedResponse;

@Tag(name = "MOLLU 타임 관련 API")
@RestController
@RequestMapping("/contents/mollu-time")
@RequiredArgsConstructor
public class MolluTimeController {

    private final MolluTimeService molluTimeService;

    @Operation(summary = "MOLLU 타임 조회")
    @OkResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping
    public ResponseEntity<SearchMolluTimeResponse> searchMolluTime(@Login String memberId) {
        return ResponseEntity.ok(molluTimeService.searchMolluTime(memberId));
    }
}
