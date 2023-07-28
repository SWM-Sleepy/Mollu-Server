package sleepy.mollu.server.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select case when count(m) > 0 then true else false end from Member m where m.molluId.value = :molluId")
    boolean existsByMolluId(@Param("molluId") String molluId);

    @Query("select m from Member m join Preference p on m.preference = p where p.molluAlarm = true")
    List<Member> findAllByMolluAlarmAllowed();
}
