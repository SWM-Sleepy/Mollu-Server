package sleepy.mollu.server.member.profile.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record ProfileRequest(String molluId, String name, LocalDate birthday, MultipartFile profileFile) {
}
