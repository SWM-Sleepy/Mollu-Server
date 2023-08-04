package sleepy.mollu.server.alarm.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.alarm.domain.MolluAlarm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MolluAlarmRepositoryTest extends RepositoryTest {

    @Autowired
    private MolluAlarmRepository molluAlarmRepository;

    @Test
    @DisplayName("오늘, 어제의 MOLLU 타임을 조회한다.")
    void MolluAlarmRepositoryTest() {
        // given
        final MolluAlarm yesterdayMolluAlarm = molluAlarmRepository.save(MolluAlarm.builder()
                .molluTime(NOW.minusDays(1))
                .send(false)
                .build());

        final MolluAlarm todayMolluAlarm = molluAlarmRepository.save(MolluAlarm.builder()
                .molluTime(NOW)
                .send(false)
                .build());

        // when
        final MolluAlarm top = molluAlarmRepository.findTop().orElseThrow();
        final MolluAlarm secondTop = molluAlarmRepository.findSecondTop().orElseThrow();

        // then
        assertAll(
                () -> assertThat(top).isSameAs(todayMolluAlarm),
                () -> assertThat(secondTop).isSameAs(yesterdayMolluAlarm)
        );
    }
}