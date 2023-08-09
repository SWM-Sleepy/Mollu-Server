package sleepy.mollu.server.content.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

public interface ContentReportRepository extends JpaRepository<ContentReport, Long> {

    List<ContentReport> findAllByMember(Member member);
}
