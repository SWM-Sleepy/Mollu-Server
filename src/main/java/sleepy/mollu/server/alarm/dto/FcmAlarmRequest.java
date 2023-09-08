package sleepy.mollu.server.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record FcmAlarmRequest(@JsonProperty("registration_ids") List<String> registrationIds, Notification notification,
                              Data data) {

    public record Notification(String title, String body) {
    }

    public record Data(String title, String body) {
    }
}
