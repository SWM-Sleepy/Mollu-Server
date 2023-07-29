package sleepy.mollu.server.alarm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.alarm.domain.MolluAlarm;

import java.util.List;
import java.util.Optional;

public interface MolluAlarmRepository extends JpaRepository<MolluAlarm, Long> {

    Optional<MolluAlarm> findTopByOrderByIdDesc();

    List<MolluAlarm> findAllByOrderByIdDesc();
}
