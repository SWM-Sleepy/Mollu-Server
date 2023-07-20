package sleepy.mollu.server.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.content.domain.content.Content;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    private String id;

    @Embedded
    private Name name;

    @Embedded
    private MolluId molluId;

    @Embedded
    private Birthday birthday;

    @OneToMany(mappedBy = "member")
    private List<Content> contents;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @Builder
    public Member(String id, String name, String molluId, LocalDate birthday, Preference preference) {
        this.id = id;
        this.name = new Name(name);
        this.molluId = new MolluId(molluId);
        this.birthday = new Birthday(birthday);
        setPreference(preference);
    }

    private void setPreference(Preference preference) {
        this.preference = preference;
        preference.setMember(this);
    }

    public boolean isSameId(String id) {
        return this.id.equals(id);
    }
}
