package sleepy.mollu.server.alarm.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class MolluAlarm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime molluTime;

    private String question;

    private boolean send;

    @Builder
    public MolluAlarm(LocalDateTime molluTime, String question, boolean send) {
        this.molluTime = molluTime;
        this.question = question;
        this.send = send;
    }

    public boolean isToday(LocalDateTime now) {
        return this.molluTime.getYear() == now.getYear() &&
                this.molluTime.getMonth() == now.getMonth() &&
                this.molluTime.getDayOfMonth() == now.getDayOfMonth();
    }

    public void updateSend() {
        this.send = true;
    }

    public void updateTime(LocalDateTime molluTime) {
        this.molluTime = molluTime;
    }
}
