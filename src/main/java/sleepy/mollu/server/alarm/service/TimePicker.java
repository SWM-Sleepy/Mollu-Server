package sleepy.mollu.server.alarm.service;

import java.time.LocalTime;

@FunctionalInterface
public interface TimePicker {

    LocalTime pick(LocalTime from, LocalTime to);
}
