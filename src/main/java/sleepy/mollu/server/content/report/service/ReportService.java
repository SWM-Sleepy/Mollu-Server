package sleepy.mollu.server.content.report.service;

import sleepy.mollu.server.content.report.dto.ReportRequest;

public interface ReportService {

    void reportContent(String memberId, String contentId, ReportRequest request);
}
