package sleepy.mollu.server.alarm.dto;

public record FcmAlarmRequest(String to, Notification notification, Data data) {

    public record Notification(String title, String body) {
    }

    public record Data(String title, String body) {
    }
}
