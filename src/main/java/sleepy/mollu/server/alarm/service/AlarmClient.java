package sleepy.mollu.server.alarm.service;

import java.util.List;

public interface AlarmClient {

    void send(List<String> to, String question);
}
