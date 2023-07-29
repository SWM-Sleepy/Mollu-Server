package sleepy.mollu.server.admin.alarm.dto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record MolluRangeResponse(LocalTime from, LocalTime to) {

    public String getFromString() {
        return from.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getToString() {
        return to.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
