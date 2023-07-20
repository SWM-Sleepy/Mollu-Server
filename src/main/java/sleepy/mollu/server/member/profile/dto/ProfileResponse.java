package sleepy.mollu.server.member.profile.dto;

import java.time.LocalDate;

public record ProfileResponse(String memberId, String molluId, String name, LocalDate birthday) {
}
