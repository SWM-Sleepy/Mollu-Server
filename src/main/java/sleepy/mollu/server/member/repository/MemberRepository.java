package sleepy.mollu.server.member.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select case when count(m) > 0 then true else false end from Member m where m.molluId.value = :molluId")
    boolean existsByMolluId(@Param("molluId") String molluId);

    @Query("select m from Member m join m.preference p where p.molluAlarm = true")
    List<Member> findAllByMolluAlarmAllowed();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from Member m where m.id = :memberId")
    Optional<Member> findByIdForUpdate(@Param("memberId") String memberId);
}
