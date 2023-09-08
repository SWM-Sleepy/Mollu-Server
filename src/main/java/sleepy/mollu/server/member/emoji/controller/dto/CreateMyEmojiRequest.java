package sleepy.mollu.server.member.emoji.controller.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateMyEmojiRequest(@NotNull String emoji, @NotNull MultipartFile emojiFile) {
}
