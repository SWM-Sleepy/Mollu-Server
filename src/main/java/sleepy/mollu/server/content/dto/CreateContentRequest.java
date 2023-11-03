package sleepy.mollu.server.content.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public record CreateContentRequest(

        @NotNull
        String location,

        String tag,

        String question,

        LocalDateTime molluDateTime,

        @NotNull
        LocalDateTime uploadDateTime,

        @NotNull
        List<String> groups,

        @NotNull
        MultipartFile frontContentFile,

        @NotNull
        MultipartFile backContentFile) {
}
