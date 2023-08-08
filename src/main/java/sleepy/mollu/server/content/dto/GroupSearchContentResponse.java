package sleepy.mollu.server.content.dto;

import java.time.LocalDateTime;

public record GroupSearchContentResponse(Member member, Content content) {

    public record Member(String memberId, String molluId, String name, String profileSource) {
    }

    public record Content(String contentId, String location, String groupName, LocalDateTime molluDateTime,
                          LocalDateTime uploadDateTime, String tag, String frontContentSource,
                          String backContentSource) {
    }
}