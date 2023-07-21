package sleepy.mollu.server.content.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.report.dto.ReportRequest;
import sleepy.mollu.server.content.repository.ContentRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ContentRepository contentRepository;

    @Override
    public void reportContent(String memberId, String contentId, ReportRequest request) {


    }
}
