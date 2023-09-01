package sleepy.mollu.server.member.domain.content;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class SearchRange {

    private static final int FROM_HOUR = 0;
    private static final int FROM_MINUTE = 0;
    private static final int TO_HOUR = 23;
    private static final int TO_MINUTE = 59;
    private static final int SEARCH_PERIOD = 7;

    private final LocalDateTime from;
    private final LocalDateTime to;

    private SearchRange(LocalDate localDate) {
        this.from = getFromDateTime(localDate.minusDays(SEARCH_PERIOD));
        this.to = getToDateTime(localDate);
    }

    public static SearchRange from(LocalDate localDate) {
        return new SearchRange(localDate);
    }

    private LocalDateTime getFromDateTime(LocalDate date) {
        return date.atTime(FROM_HOUR, FROM_MINUTE);
    }

    private LocalDateTime getToDateTime(LocalDate date) {
        return date.atTime(TO_HOUR, TO_MINUTE);
    }
}
