package sleepy.mollu.server.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.member.controller.dto.MyContentsResponse;
import sleepy.mollu.server.member.service.MemberService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

import java.time.LocalDate;

@Tag(name = "멤버 관련 API")
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "내 컨텐츠 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/contents")
    public ResponseEntity<MyContentsResponse> searchMyContents(@Login String memberId, @RequestParam LocalDate date) {

        return ResponseEntity.ok(memberService.searchMyContents(memberId, date));
    }
}
