package sleepy.mollu.server.content.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sleepy.mollu.server.common.domain.CustomPageRequest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.service.ContentService;

@RestController
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @GetMapping("/contents/group")
    public ResponseEntity<GroupSearchFeedResponse> groupSearchFeedResponse(@RequestParam("page") int page) {
        return ResponseEntity.ok().body(contentService.searchGroupFeed(CustomPageRequest.of(page)));
    }
}
