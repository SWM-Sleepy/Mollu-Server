package sleepy.mollu.server.member.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Embedded
    private MolluId molluId;

    @Embedded
    private Birthday birthday;

    @Builder
    public Member(String id, String name, String molluId, LocalDate birthday) {
        this.id = id;
        this.name = new Name(name);
        this.molluId = new MolluId(molluId);
        this.birthday = new Birthday(birthday);
    }
}
