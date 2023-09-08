package sleepy.mollu.server.content.reaction.controller.dto;

import jakarta.validation.constraints.NotNull;

public record CreateReactionRequest(@NotNull String emoji) {
}
