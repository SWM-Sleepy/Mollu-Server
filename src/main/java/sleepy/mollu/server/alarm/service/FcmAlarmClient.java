package sleepy.mollu.server.alarm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sleepy.mollu.server.alarm.dto.FcmAlarmRequest;
import sleepy.mollu.server.alarm.dto.FcmAlarmResponse;

@Component
public class FcmAlarmClient implements AlarmClient {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

    @Value("${alarm.fcm.authorization-key}")
    private String authorizationKey;

    @Override
    public void send(String to) {
        final HttpHeaders headers = getHeaders();
        FcmAlarmRequest request = getRequest(to);
        final HttpEntity<FcmAlarmRequest> requestEntity = new HttpEntity<>(request, headers);

        REST_TEMPLATE.postForEntity(FCM_URL, requestEntity, FcmAlarmResponse.class);
    }

    private FcmAlarmRequest getRequest(String to) {
        FcmAlarmRequest.Notification notification = new FcmAlarmRequest.Notification("title", "body");
        FcmAlarmRequest.Data data = new FcmAlarmRequest.Data("title", "body");

        return new FcmAlarmRequest(to, notification, data);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + authorizationKey);

        return headers;
    }
}
