package sleepy.mollu.server.alarm.service;

import java.util.List;

@FunctionalInterface
public interface Picker {
    String pick(List<String> items);
}
