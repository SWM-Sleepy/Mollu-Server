package sleepy.mollu.server.content.reaction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.content.reaction.controller.dto.CreateReactionRequest;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionResponse;
import sleepy.mollu.server.content.reaction.service.ContentReactionService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

import java.net.URI;

@Tag(name = "컨텐츠 반응 관련 API")
@RestController
@RequestMapping("/contents/{contentId}/reactions")
@RequiredArgsConstructor
public class ContentReactionController {

    private final ContentReactionService contentReactionService;

    @Operation(summary = "컨텐츠에 반응 추가")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping
    public ResponseEntity<Void> createReaction(@Login String memberId,
                                               @PathVariable String contentId,
                                               @RequestBody @Valid CreateReactionRequest request) {

        final String reactionId = contentReactionService.createReaction(memberId, contentId, request.emoji());
        final URI uri = URI.create("/contents/" + contentId + "/reactions/" + reactionId);
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "컨텐츠 반응 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping
    public ResponseEntity<SearchReactionResponse> searchReaction(@Login String memberId,
                                                                 @PathVariable String contentId) {

        return ResponseEntity.ok(contentReactionService.searchReaction(memberId, contentId));
    }

    @Operation(summary = "컨텐츠 반응 삭제")
    @NoContentResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @DeleteMapping("/{reactionId}")
    public ResponseEntity<Void> deleteReaction(@Login String memberId,
                                               @PathVariable String contentId,
                                               @PathVariable String reactionId) {

        contentReactionService.deleteReaction(memberId, contentId, reactionId);
        return ResponseEntity.noContent().build();
    }

}
