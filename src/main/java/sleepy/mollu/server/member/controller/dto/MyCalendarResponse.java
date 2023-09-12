package sleepy.mollu.server.member.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MyCalendarResponse(List<CalendarResponse> calendar) {

    public record CalendarResponse(String contentId, LocalDateTime uploadDateTime, String thumbnailUrl) {

    }
}
