package sleepy.mollu.server.member.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MyContentsResponse(List<MyContent> feed) {

    public record MyContent(String contentId,
                            String location,
                            String groupName,
                            LocalDateTime molluDateTime,
                            LocalDateTime uploadDateTime,
                            String tag,
                            String frontContentSource,
                            String backContentSource) {
    }
}
