package sleepy.mollu.server.common.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LambdaRequest(List<String> tokens, String title, String body, @JsonProperty("os_type") String osType) {
}
