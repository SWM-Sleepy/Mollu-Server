package sleepy.mollu.server.content.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GroupSearchFeedResponse(String cursorId, LocalDateTime cursorEndDate,
                                      List<GroupSearchContentResponse> feed) {

}
