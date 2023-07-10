package sleepy.mollu.server.content.dto;

import java.time.LocalDateTime;

public record GroupSearchContentResponse(
        String contentId,
        String memberUUID,
        String memberId,
        String memberName,
        String location,
        String groupName,
        LocalDateTime limitDateTime,
        LocalDateTime uploadDateTime,
        String tag,
        String frontContentSrc,
        String backContentSrc) {

}