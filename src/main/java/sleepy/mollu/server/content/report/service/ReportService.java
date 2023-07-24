package sleepy.mollu.server.content.report.service;

import sleepy.mollu.server.content.report.dto.ReportRequest;

public interface ReportService {

    Long reportContent(String memberId, String contentId, ReportRequest request);
}
