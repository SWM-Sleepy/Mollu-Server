package sleepy.mollu.server.group.controller.dto;

import jakarta.validation.constraints.NotNull;

public record JoinGroupByCodeRequest(@NotNull String code) {
}
