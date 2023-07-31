package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ContentTime {

    private LocalDateTime molluDateTime;
    private LocalDateTime uploadDateTime;

    public static ContentTime of(LocalDateTime molluDateTime, LocalDateTime uploadDateTime) {
        return new ContentTime(molluDateTime, uploadDateTime);
    }
}
