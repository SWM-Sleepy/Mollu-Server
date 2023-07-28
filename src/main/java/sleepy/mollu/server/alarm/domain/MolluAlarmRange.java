package sleepy.mollu.server.alarm.domain;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MolluAlarmRange {

    private static final MolluAlarmRange INSTANCE = new MolluAlarmRange();

    private LocalTime from = LocalTime.of(10, 0);
    private LocalTime to = LocalTime.of(22, 0);

    private MolluAlarmRange() {
    }

    public static MolluAlarmRange getInstance() {
        return INSTANCE;
    }

    public boolean isInRange(LocalTime now) {
        return now.isAfter(from) && now.isBefore(to);
    }

    public void update(LocalTime from, LocalTime to) {
        this.from = from;
        this.to = to;
    }
}
