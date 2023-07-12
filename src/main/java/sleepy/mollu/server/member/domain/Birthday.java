package sleepy.mollu.server.member.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@NoArgsConstructor
public class Birthday {

    private LocalDate date;

    public Birthday(@NotNull LocalDate date) {
        this.date = date;
    }
}
