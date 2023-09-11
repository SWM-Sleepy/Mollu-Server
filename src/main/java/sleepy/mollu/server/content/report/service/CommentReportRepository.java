package sleepy.mollu.server.content.report.service;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.report.domain.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

}
