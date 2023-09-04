package sleepy.mollu.server.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.common.domain.FileSource;
import sleepy.mollu.server.member.emoji.domain.Emoji;

import java.time.LocalDate;

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

    private String refreshToken;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "profile_source"))
    private FileSource profileSource = new FileSource("");

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emoji_id")
    private Emoji emoji;

    @Builder
    public Member(String id, String name, String molluId, LocalDate birthday, String refreshToken, Preference preference) {
        this.id = id;
        this.name = new Name(name);
        this.molluId = new MolluId(molluId);
        this.birthday = new Birthday(birthday);
        this.refreshToken = refreshToken;
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

    public void updatePhoneToken(String phoneToken) {
        this.phoneToken = phoneToken;
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

    public boolean hasSameRefreshToken(String refreshToken) {
        return this.refreshToken.equals(refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public boolean hasEmoji() {
        return this.emoji != null;
    }

    public void createEmoji() {
        this.emoji = new Emoji();
        this.emoji.assignMember(this);
    }

    public void updateEmoji(String emojiType, String emojiSource) {
        this.emoji.update(emojiType, emojiSource);
    }

    public void deleteEmoji(String emojiType) {
        this.emoji.delete(emojiType);
    }
}
