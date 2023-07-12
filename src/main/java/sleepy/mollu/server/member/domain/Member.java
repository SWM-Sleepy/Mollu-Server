package sleepy.mollu.server.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    private String id;

    @Embedded
    private Name name;

    @Email
    private String email;

    @Embedded
    private MolluId molluId;

    @Embedded
    private Birthday birthday;

    @Builder
    public Member(String id, String name, String email, String molluId, LocalDate birthday) {
        this.id = id;
        this.name = new Name(name);
        this.email = email;
        this.molluId = new MolluId(molluId);
        this.birthday = new Birthday(birthday);
    }
}
