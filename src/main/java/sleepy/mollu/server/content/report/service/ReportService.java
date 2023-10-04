package sleepy.mollu.server.content.report.service;

public interface ReportService {

    Long reportContent(String memberId, String contentId, String reason);

    Long reportComment(String memberId, String contentId, String commentId, String reason);
}
