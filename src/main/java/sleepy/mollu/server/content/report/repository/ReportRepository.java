package sleepy.mollu.server.content.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.report.domain.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
