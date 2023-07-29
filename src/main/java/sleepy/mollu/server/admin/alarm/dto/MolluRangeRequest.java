package sleepy.mollu.server.admin.alarm.dto;

import java.time.LocalTime;

public record MolluRangeRequest(LocalTime from, LocalTime to) {
}
