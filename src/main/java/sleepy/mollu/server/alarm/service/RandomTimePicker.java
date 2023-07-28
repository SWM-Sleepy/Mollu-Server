package sleepy.mollu.server.alarm.service;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
public class RandomTimePicker implements TimePicker {

    @Override
    public LocalTime pick(LocalTime from, LocalTime to) {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("[from]은 [to]보다 이전 시각이어야 합니다.");
        }

        final int differenceSeconds = (int) from.until(to, ChronoUnit.SECONDS);
        final long randomSeconds = (long) (Math.random() * differenceSeconds);

        return from.plusSeconds(randomSeconds);
    }
}
