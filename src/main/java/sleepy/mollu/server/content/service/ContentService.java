package sleepy.mollu.server.content.service;

import sleepy.mollu.server.content.controller.dto.SearchContentResponse;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;

import java.time.LocalDateTime;

public interface ContentService {

    GroupSearchFeedResponse searchGroupFeed(String memberId, String cursorId, LocalDateTime cursorEndDate);

    String createContent(String memberId, CreateContentRequest request);

    void deleteContent(String memberId, String contentId);

    SearchContentResponse searchContent(String memberId, String contentId);
}
