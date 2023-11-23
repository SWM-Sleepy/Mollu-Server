package sleepy.mollu.server.common.domain;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.common.dto.LambdaRequest;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Platform;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationImpl implements NotificationHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AWSLambda awsLambda;

    @Override
    public void send(List<Member> members, String title, String body) {
        final List<String> androidPhoneTokens = getPhoneTokens(members, Platform.ANDROID);
        final List<String> iosPhoneTokens = getPhoneTokens(members, Platform.IOS);

        send(androidPhoneTokens, title, body, "ANDROID");
        send(iosPhoneTokens, title, body, "IOS");
    }

    private void send(List<String> tokens, String title, String body, String osType) {
        final LambdaRequest payload = new LambdaRequest(tokens, title, body, osType);
        final InvokeRequest request = new InvokeRequest()
                .withFunctionName("notification-service")
                .withPayload(toJson(payload));
        awsLambda.invoke(request);
    }

    private List<String> getPhoneTokens(List<Member> members, Platform platform) {
        return members.stream()
                .filter(member -> member.hasSamePlatform(platform))
                .map(Member::getPhoneToken)
                .toList();
    }

    private String toJson(LambdaRequest payload) {
        try {
            return OBJECT_MAPPER.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("잘못된 payload 입니다.");
        }
    }
}
