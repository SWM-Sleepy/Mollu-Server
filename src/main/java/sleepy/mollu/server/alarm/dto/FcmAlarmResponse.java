package sleepy.mollu.server.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


// TODO: Result 필드 추가
public record FcmAlarmResponse(@JsonProperty("multicast_id") String multicastId, int success, int failure,
                               @JsonProperty("canonical_ids") String canonicalId) {
}
