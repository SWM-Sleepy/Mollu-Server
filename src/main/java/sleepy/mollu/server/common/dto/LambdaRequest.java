package sleepy.mollu.server.common.dto;

import java.util.List;

public record LambdaRequest(List<String> tokens, String title, String body, String osType) {
}
