package sleepy.mollu.server.member.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class Preference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private long id;

    private boolean molluAlarm;

    private boolean contentAlarm;

    @OneToOne(mappedBy = "preference", fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Preference(boolean molluAlarm, boolean contentAlarm, Member member) {
        this.molluAlarm = molluAlarm;
        this.contentAlarm = contentAlarm;
        this.member = member;
    }

    public void update(boolean molluAlarm, boolean contentAlarm) {
        this.molluAlarm = molluAlarm;
        this.contentAlarm = contentAlarm;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
