package sleepy.mollu.server.member.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SignupRequest(@NotNull String name, @NotNull LocalDate birthday, @NotNull String molluId) {
}
