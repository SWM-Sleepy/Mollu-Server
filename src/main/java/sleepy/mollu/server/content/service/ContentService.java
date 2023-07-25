package sleepy.mollu.server.content.service;

import org.springframework.data.domain.Pageable;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;

public interface ContentService {

    GroupSearchFeedResponse searchGroupFeed(String memberId, Pageable pageable);

    String createContent(String memberId, CreateContentRequest request);

    void deleteContent(String memberId, String contentId);
}
