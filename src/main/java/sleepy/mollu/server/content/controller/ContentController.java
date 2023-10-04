package sleepy.mollu.server.content.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.content.controller.dto.SearchContentResponse;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.service.ContentService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

import java.net.URI;
import java.time.LocalDateTime;

@Tag(name = "컨텐츠 관련 API")
@RestController
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @Operation(summary = "그룹 피드 검색")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/group")
    public ResponseEntity<GroupSearchFeedResponse> groupSearchFeedResponse(@Login String memberId,
                                                                           @RequestParam(value = "cursorId", required = false) String cursorId,
                                                                           @RequestParam(value = "cursorEndDate", required = false) LocalDateTime cursorEndDate) {

        return ResponseEntity.ok(contentService.searchGroupFeed(memberId, cursorId, cursorEndDate));
    }

    @Operation(summary = "컨텐츠 업로드")
    @CreatedResponse
    @InternalServerErrorResponse
    @PostMapping
    public ResponseEntity<Void> createContent(@Login String memberId, @ModelAttribute @Valid CreateContentRequest request) {

        final String contentId = contentService.createContent(memberId, request);
        final URI uri = URI.create("/contents/" + contentId);

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "컨텐츠 삭제")
    @NoContentResponse
    @InternalServerErrorResponse
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> deleteContent(@Login String memberId, @PathVariable String contentId) {

        contentService.deleteContent(memberId, contentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "컨텐츠 자세히 보기")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/{contentId}")
    public ResponseEntity<SearchContentResponse> searchContent(@Login String memberId, @PathVariable String contentId) {

        return ResponseEntity.ok(contentService.searchContent(memberId, contentId));
    }
}
