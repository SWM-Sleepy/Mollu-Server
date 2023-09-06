package sleepy.mollu.server.member.preference.dto;

import jakarta.validation.constraints.NotNull;

public record PhoneTokenRequest(@NotNull String phoneToken, @NotNull String platform) {
}
