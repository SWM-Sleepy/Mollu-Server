package sleepy.mollu.server.alarm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import sleepy.mollu.server.alarm.dto.FcmAlarmRequest;
import sleepy.mollu.server.alarm.dto.FcmAlarmRequest.Data;
import sleepy.mollu.server.alarm.dto.FcmAlarmRequest.Notification;
import sleepy.mollu.server.alarm.dto.FcmAlarmResponse;
import sleepy.mollu.server.alarm.dto.FcmAlarmResponse.FcmResult;

import java.util.List;
import java.util.stream.IntStream;

@Component
@Slf4j
public class FcmAlarmClient implements AlarmClient {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String MOLLU_TIME_TITLE = "It's MOLLU Time!";

    @Value("${alarm.fcm.authorization-key}")
    private String authorizationKey;

    @Override
    public void send(List<String> phoneTokens, String question) {

        if (phoneTokens.isEmpty()) {
            log.warn("MOLLU 알림의 수신자가 없습니다.");
            return;
        }

        final FcmAlarmRequest request = getRequest(phoneTokens, question);
        final HttpHeaders headers = getHeaders();
        final HttpEntity<FcmAlarmRequest> requestEntity = new HttpEntity<>(request, headers);
        final FcmAlarmResponse response = REST_TEMPLATE.postForEntity(FCM_URL, requestEntity, FcmAlarmResponse.class).getBody();
        logResponse(phoneTokens, response);
    }

    private FcmAlarmRequest getRequest(List<String> phoneTokens, String question) {
        Notification notification = new Notification(MOLLU_TIME_TITLE, question);
        Data data = new Data(MOLLU_TIME_TITLE, question);

        return new FcmAlarmRequest(phoneTokens, notification, data);
    }

    private HttpHeaders getHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + authorizationKey);

        return headers;
    }

    private void logResponse(List<String> phoneTokens, FcmAlarmResponse response) {
        if (response == null) {
            log.warn("FCM 서버로부터 응답이 없습니다.");
            return;
        }

        log.info("MOLLU 알림 전송 성공: {}", response.success());
        log.info("MOLLU 알림 전송 실패: {}", response.failure());

        final List<FcmResult> results = response.results();
        IntStream.range(0, phoneTokens.size())
                .filter(i -> results.get(i).error() != null)
                .forEach(i -> {
                    final String phoneToken = phoneTokens.get(i);
                    final String error = results.get(i).error();

                    log.warn("Phone Token: {}, MOLLU 알림 전송 실패 이유: {}", phoneToken, error);
                });
    }
}
