package sleepy.mollu.server.alarm.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.domain.MolluAlarmRange;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;

import java.time.*;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MolluAlarmScheduler {

    private static final LocalDate NOW = LocalDate.now();
    private final MolluAlarmRepository molluAlarmRepository;
    private final TimePicker timePicker;
    private final TaskScheduler taskScheduler;
    private final AlarmService alarmService;
    @Value("${spring.profiles.active}")
    private String profile;

    @PostConstruct
    public void init() {

        if (!isValidProfile()) return;

        final MolluAlarm molluAlarm = getMolluAlarm();
        if (molluAlarm == null) return;
        sendAlarm(molluAlarm);
    }

    private boolean isValidProfile() {
        if (profile == null) return false;
        final boolean isProfileDev = profile.equals("dev");
        final boolean isProfileProd = profile.equals("prod");

        return isProfileDev || isProfileProd;
    }

    private MolluAlarm getMolluAlarm() {
        final Optional<MolluAlarm> newMolluAlarm = molluAlarmRepository.findTop();

        if (newMolluAlarm.isEmpty()) {
            generateAlarm(NOW);
            generateAlarm(NOW.minusDays(1));
            scheduleAlarm();
            return null;
        }

        return newMolluAlarm.get();
    }

    private void sendAlarm(MolluAlarm molluAlarm) {
        if (!molluAlarm.isToday(LocalDateTime.now())) {
            generateAlarm(NOW);
        }

        if (!molluAlarm.isSend()) {
            scheduleAlarm();
        }
    }

    private void generateAlarm(LocalDate currentDate) {
        final MolluAlarmRange molluAlarmRange = MolluAlarmRange.getInstance();
        final LocalTime sendTime = timePicker.pick(molluAlarmRange.getFrom(), molluAlarmRange.getTo());
        final LocalDateTime molluTime = LocalDateTime.of(currentDate, sendTime);

        molluAlarmRepository.save(new MolluAlarm(molluTime, false));
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void scheduleAlarm() {
        final Instant startTime = getStartTime();

        taskScheduler.schedule(this::sendAlarm, startTime);
    }

    private Instant getStartTime() {
        final MolluAlarm molluAlarm = molluAlarmRepository.findTop()
                .orElseThrow();
        final LocalDateTime molluTime = molluAlarm.getMolluTime();
        final ZoneId zoneId = ZoneId.systemDefault();
        final ZonedDateTime zonedDateTime = molluTime.atZone(zoneId);

        return zonedDateTime.toInstant();
    }

    private void sendAlarm() {
        alarmService.sendAlarm();
    }
}
