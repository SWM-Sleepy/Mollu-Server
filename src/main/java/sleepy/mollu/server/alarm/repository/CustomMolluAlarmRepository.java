package sleepy.mollu.server.alarm.repository;

import sleepy.mollu.server.alarm.domain.MolluAlarm;

import java.util.Optional;

public interface CustomMolluAlarmRepository {

    Optional<MolluAlarm> findSecondTop();
}
