package sleepy.mollu.server.alarm.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.TaskScheduler;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MolluAlarmSchedulerTest {

    @InjectMocks
    private MolluAlarmScheduler molluAlarmScheduler;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private TimePicker timePicker;

    @Mock
    private Picker picker;

    @Mock
    private MolluAlarmRepository molluAlarmRepository;

    @Mock
    private AlarmService alarmService;

    @Nested
    @DisplayName("[스케쥴러 실행시] ")
    class SchedulerTest {

        final String question = "question";

        @Test
        @DisplayName("어떤 알림도 생성되지 않았으면 새로운 알림을 생성하고, 알림을 예약한다.")
        void SchedulerTest0() {
            // given
            given(molluAlarmRepository.findTop()).willReturn(Optional.empty());
            given(timePicker.pick(any(LocalTime.class), any(LocalTime.class))).willReturn(LocalTime.now());
            given(picker.pick(anyList())).willReturn(question);
            final MolluAlarm molluAlarm = mock(MolluAlarm.class);
            given(molluAlarmRepository.findTop()).willReturn(Optional.of(molluAlarm));
            given(molluAlarm.getMolluTime()).willReturn(LocalDateTime.now());

            // when
            molluAlarmScheduler.init();

            // then
            then(molluAlarmRepository).should(times(1)).save(any(MolluAlarm.class));
            then(taskScheduler).should(times(1)).schedule(any(Runnable.class), any(Instant.class));
        }

        @Test
        @DisplayName("오늘 생성된 알림이 없으면 새로운 알림을 생성한다.")
        void SchedulerTest2() {
            // given
            final MolluAlarm molluAlarm = mock(MolluAlarm.class);
            given(molluAlarmRepository.findTop()).willReturn(Optional.of(molluAlarm));
            given(molluAlarm.isToday(any())).willReturn(false);
            given(timePicker.pick(any(LocalTime.class), any(LocalTime.class))).willReturn(LocalTime.now());
            given(picker.pick(anyList())).willReturn(question);
            given(molluAlarm.getMolluTime()).willReturn(LocalDateTime.now());

            // when
            molluAlarmScheduler.init();

            // then
            then(molluAlarmRepository).should(times(1)).save(any(MolluAlarm.class));
        }

        @Test
        @DisplayName("오늘 이미 알람을 보냈다면 실행하지 않는다.")
        void SchedulerTest3() {
            // given
            final MolluAlarm molluAlarm = mock(MolluAlarm.class);
            given(molluAlarmRepository.findTop()).willReturn(Optional.of(molluAlarm));
            given(molluAlarm.isToday(any())).willReturn(true);
            given(molluAlarm.isSend()).willReturn(true);

            // when
            molluAlarmScheduler.init();

            // then
            then(taskScheduler).should(times(0)).schedule(any(Runnable.class), any(Instant.class));
        }

        @Test
        @DisplayName("알람을 성공적으로 보낸다.")
        void SchedulerTest4() {
            // given
            final MolluAlarm molluAlarm = mock(MolluAlarm.class);
            given(molluAlarmRepository.findTop()).willReturn(Optional.of(molluAlarm));
            given(molluAlarm.isToday(any())).willReturn(true);
            given(molluAlarm.isSend()).willReturn(false);
            given(molluAlarm.getMolluTime()).willReturn(LocalDateTime.now());
            given(taskScheduler.schedule(any(Runnable.class), any(Instant.class))).willAnswer(invocation -> {
                final Runnable task = invocation.getArgument(0);
                task.run();
                return null;
            });

            // when
            molluAlarmScheduler.init();

            // then
            then(alarmService).should(times(1)).sendAlarm();
        }
    }

}