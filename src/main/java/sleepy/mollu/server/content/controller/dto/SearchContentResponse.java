package sleepy.mollu.server.content.controller.dto;

import java.time.LocalDateTime;

public record SearchContentResponse(String contentId, String location, String groupName,
                                    LocalDateTime molluDateTime, LocalDateTime uploadDateTime, String tag,
                                    String frontContentSource, String backContentSource) {
}
