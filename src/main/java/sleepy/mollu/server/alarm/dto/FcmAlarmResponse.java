package sleepy.mollu.server.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record FcmAlarmResponse(@JsonProperty("multicast_id") String multicastId, int success, int failure,
                               @JsonProperty("canonical_ids") List<FcmResult> results) {

    public record FcmResult(@JsonProperty("message_id") String messageId, String error) {
    }
}
