package sleepy.mollu.server.content.service;

import org.springframework.data.domain.Pageable;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;

public interface ContentService {

    GroupSearchFeedResponse searchGroupFeed(Pageable pageable);
}
