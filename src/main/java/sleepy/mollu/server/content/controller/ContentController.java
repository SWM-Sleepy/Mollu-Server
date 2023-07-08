package sleepy.mollu.server.content.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.common.domain.CustomPageRequest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.service.ContentService;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

@Tag(name = "컨텐츠 관련 API")
@RestController
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @Operation(summary = "그룹 피드 검색")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/contents/group")
    public ResponseEntity<GroupSearchFeedResponse> groupSearchFeedResponse(@RequestParam("page") int page) {
        return ResponseEntity.ok(contentService.searchGroupFeed(CustomPageRequest.of(page)));
    }
}
