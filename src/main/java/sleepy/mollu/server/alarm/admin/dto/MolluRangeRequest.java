package sleepy.mollu.server.alarm.admin.dto;

import java.time.LocalTime;

public record MolluRangeRequest(LocalTime from, LocalTime to) {
}
