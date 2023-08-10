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
        validateTime(from, to);
        this.from = from;
        this.to = to;
    }

    private void validateTime(LocalTime from, LocalTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("from[" + from + "]은 to[" + to + "]보다 빠를 수 없습니다.");
        }
    }
}
