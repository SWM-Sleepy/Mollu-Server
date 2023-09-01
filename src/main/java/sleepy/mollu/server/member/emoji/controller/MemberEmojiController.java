package sleepy.mollu.server.member.emoji.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.member.emoji.controller.dto.CreateMyEmojiRequest;
import sleepy.mollu.server.member.emoji.controller.dto.SearchMyEmojiResponse;
import sleepy.mollu.server.member.emoji.service.MemberEmojiService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

@Tag(name = "내 이모티콘 관련 API")
@RestController
@RequestMapping("/members/emojis")
@RequiredArgsConstructor
public class MemberEmojiController {

    private final MemberEmojiService memberEmojiService;

    @Operation(summary = "내 이모티콘 등록")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping
    public ResponseEntity<Void> createMyEmoji(@Login String memberId, @ModelAttribute @Valid CreateMyEmojiRequest request) {

        memberEmojiService.createMyEmoji(memberId, request.emoji(), request.emojiFile());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "내 이모티콘 조회")
    @OkResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping
    public ResponseEntity<SearchMyEmojiResponse> searchMyEmoji(@Login String memberId) {

        return ResponseEntity.ok().body(memberEmojiService.searchMyEmoji(memberId));
    }
}
