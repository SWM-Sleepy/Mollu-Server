package sleepy.mollu.server.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.common.domain.FileSource;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.Report;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

    private String phoneToken;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "profile_source"))
    private FileSource profileSource = new FileSource("");

    @OneToMany(mappedBy = "member")
    private List<Content> contents = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @OneToMany(mappedBy = "member")
    private List<Report> contentReports = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<GroupMember> groupMembers = new ArrayList<>();

    @Builder
    public Member(String id, String name, String molluId, LocalDate birthday, String phoneToken, Preference preference) {
        this.id = id;
        this.name = new Name(name);
        this.molluId = new MolluId(molluId);
        this.birthday = new Birthday(birthday);
        this.phoneToken = phoneToken;
        setPreference(preference);
    }

    private void setPreference(Preference preference) {
        this.preference = preference;
        preference.assignMember(this);
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

    public void updateProfile(String molluId, String name, LocalDate birthday, String profileSource) {
        updateMolluId(molluId);
        updateName(name);
        updateBirthday(birthday);
        updateProfileSource(profileSource);
    }

    private void updateMolluId(String molluId) {
        if (molluId != null) {
            this.molluId = new MolluId(molluId);
        }
    }

    private void updateName(String name) {
        if (name != null) {
            this.name = new Name(name);
        }
    }

    private void updateBirthday(LocalDate birthday) {
        if (birthday != null) {
            this.birthday = new Birthday(birthday);
        }
    }

    private void updateProfileSource(String profileSource) {
        if (profileSource != null) {
            this.profileSource = new FileSource(profileSource);
        }
    }

    public void addContentReport(Report report) {
        this.contentReports.add(report);
    }

    public void addGroupMember(GroupMember groupMember) {
        this.groupMembers.add(groupMember);
    }
}
