package sleepy.mollu.server.group.controller.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record CreateGroupRequest(@NotNull String name, String introduction, MultipartFile imageFile) {
}
