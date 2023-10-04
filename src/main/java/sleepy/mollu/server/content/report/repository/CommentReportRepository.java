package sleepy.mollu.server.content.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    List<CommentReport> findAllByMember(Member member);
}
