package sleepy.mollu.server.content.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record CreateContentRequest(

        @NotNull
        String location,

        String tag,

        @NotNull
        LocalDateTime molluDateTime,

        @NotNull
        LocalDateTime uploadDateTime,

        @NotNull
        MultipartFile frontContentFile,

        @NotNull
        MultipartFile backContentFile) {
}
