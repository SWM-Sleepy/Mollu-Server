package sleepy.mollu.server.content.report.service;

import sleepy.mollu.server.content.report.controller.dto.CommentReportResponse;

public interface ReportService {

    Long reportContent(String memberId, String contentId, String reason);

    CommentReportResponse reportComment(String memberId, String contentId, String commentId, String reason);
}
