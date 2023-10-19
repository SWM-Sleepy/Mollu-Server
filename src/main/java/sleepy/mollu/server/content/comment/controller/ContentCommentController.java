package sleepy.mollu.server.content.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.content.comment.controller.dto.CreateCommentRequest;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentPreviewResponse;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentResponse;
import sleepy.mollu.server.content.comment.service.ContentCommentService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

import java.net.URI;

@Tag(name = "댓글 관련 API")
@RestController
@RequestMapping("/contents/{contentId}/comments")
@RequiredArgsConstructor
public class ContentCommentController {

    private final ContentCommentService contentCommentService;

    @Operation(summary = "댓글 등록")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping
    public ResponseEntity<Void> createComment(@Login String memberId,
                                              @PathVariable String contentId,
                                              @RequestBody @Valid CreateCommentRequest request) {

        final String commentId = contentCommentService.createComment(memberId, contentId, request.comment());
        final URI uri = URI.create("/contents/" + contentId + "/comments/" + commentId);
        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "댓글 미리 보기")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/preview")
    public ResponseEntity<SearchCommentPreviewResponse> searchCommentPreview(@Login String memberId,
                                                                             @PathVariable String contentId) {

        return ResponseEntity.ok(contentCommentService.searchCommentPreview(memberId, contentId));
    }

    @Operation(summary = "댓글 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping
    public ResponseEntity<SearchCommentResponse> searchComment(@Login String memberId,
                                                               @PathVariable String contentId) {

        return ResponseEntity.ok(contentCommentService.searchComment(memberId, contentId));
    }

    @Operation(summary = "댓글 삭제")
    @NoContentResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@Login String memberId,
                                              @PathVariable String contentId,
                                              @PathVariable String commentId) {

        contentCommentService.deleteComment(memberId, contentId, commentId);
        return ResponseEntity.noContent().build();
    }


}
