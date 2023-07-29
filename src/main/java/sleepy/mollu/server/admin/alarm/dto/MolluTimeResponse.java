package sleepy.mollu.server.admin.alarm.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record MolluTimeResponse(Long id, LocalDateTime molluTime, LocalDateTime createdAt) {

    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

    public String getMolluTime() {
        return molluTime.format(PATTERN);
    }

    public String getCreatedAt() {
        return createdAt.format(PATTERN);
    }
}
