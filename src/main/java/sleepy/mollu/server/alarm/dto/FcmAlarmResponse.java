package sleepy.mollu.server.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FcmAlarmResponse(@JsonProperty("multicast_id") String multicastId, String success, String failure,
                               @JsonProperty("canonical_ids") String canonicalId) {
}
