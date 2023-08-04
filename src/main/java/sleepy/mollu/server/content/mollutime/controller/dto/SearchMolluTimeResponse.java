package sleepy.mollu.server.content.mollutime.controller.dto;

import java.time.LocalDateTime;

public record SearchMolluTimeResponse(boolean available, LocalDateTime molluTime) {
}
