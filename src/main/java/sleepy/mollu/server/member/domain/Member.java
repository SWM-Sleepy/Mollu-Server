package sleepy.mollu.server.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.content.ContentSource;
import sleepy.mollu.server.content.report.domain.Report;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    private String id;

    @Embedded
    private Name name;

    @Embedded
    private MolluId molluId;

    @Embedded
    private Birthday birthday;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "profile_source"))
    private ContentSource profileSource = new ContentSource("");

    @OneToMany(mappedBy = "member")
    private List<Content> contents = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @OneToMany(mappedBy = "member")
    private List<Report> reports = new ArrayList<>();

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

    public String getName() {
        return this.name.getValue();
    }

    public String getMolluId() {
        return this.molluId.getValue();
    }

    public LocalDate getBirthday() {
        return this.birthday.getDate();
    }

    public String getProfileSource() {
        return this.profileSource.getValue();
    }

    public void updateMolluId(String molluId) {
        this.molluId = new MolluId(molluId);
    }

    public void updateName(String name) {
        this.name = new Name(name);
    }

    public void updateBirthday(LocalDate birthday) {
        this.birthday = new Birthday(birthday);
    }

    public void updateProfileSource(String profileSource) {
        this.profileSource = new ContentSource(profileSource);
    }
}
