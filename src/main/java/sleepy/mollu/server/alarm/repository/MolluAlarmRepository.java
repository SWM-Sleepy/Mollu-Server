package sleepy.mollu.server.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sleepy.mollu.server.alarm.domain.MolluAlarm;

import java.util.List;
import java.util.Optional;

public interface MolluAlarmRepository extends JpaRepository<MolluAlarm, Long>, CustomMolluAlarmRepository {

    @Query("select ma from MolluAlarm ma order by ma.id desc limit 1")
    Optional<MolluAlarm> findTop();

    List<MolluAlarm> findAllByOrderByIdDesc();
}
