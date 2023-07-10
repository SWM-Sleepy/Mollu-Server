package sleepy.mollu.server.content.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreateContentRequest(

        @NotNull
        String location,

        @NotNull
        List<String> groupIds,

        @NotNull
        String tag,

        @NotNull
        MultipartFile frontContent,

        @NotNull
        MultipartFile backContent
) {
}
