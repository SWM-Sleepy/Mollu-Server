package sleepy.mollu.server.content.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.common.domain.CustomPageRequest;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.service.ContentService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.CreatedResponse;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.NoContentResponse;
import sleepy.mollu.server.swagger.OkResponse;

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
    public ResponseEntity<GroupSearchFeedResponse> groupSearchFeedResponse(@RequestParam("page") int page) {

        return ResponseEntity.ok(contentService.searchGroupFeed(CustomPageRequest.of(page)));
    }

    @Operation(summary = "컨텐츠 업로드")
    @CreatedResponse
    @InternalServerErrorResponse
    @PostMapping
    public ResponseEntity<Void> createContent(@ModelAttribute @Valid CreateContentRequest request) {

        contentService.createContent(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "컨텐츠 삭제")
    @NoContentResponse
    @InternalServerErrorResponse
    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> deleteContent(@Login String memberId, @PathVariable String contentId) {

        contentService.deleteContent(memberId, contentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
