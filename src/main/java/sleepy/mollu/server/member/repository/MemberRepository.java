package sleepy.mollu.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select case when count(m) > 0 then true else false end from Member m where m.molluId.value = :molluId")
    boolean existsByMolluId(@Param("molluId") String molluId);
}
