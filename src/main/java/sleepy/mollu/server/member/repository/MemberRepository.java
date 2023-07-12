package sleepy.mollu.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
