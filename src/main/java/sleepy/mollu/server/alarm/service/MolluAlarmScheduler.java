package sleepy.mollu.server.alarm.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.domain.MolluAlarmRange;
import sleepy.mollu.server.alarm.exception.FileException;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MolluAlarmScheduler {

    private static final LocalDate NOW = LocalDate.now();
    private final MolluAlarmRepository molluAlarmRepository;
    private final AlarmService alarmService;

    private final TimePicker timePicker;
    private final Picker picker;


    private final TaskScheduler taskScheduler;

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
            generateAlarm(NOW.minusDays(1));
            generateAlarm(NOW);
            registerAlarm();
            return null;
        }

        return newMolluAlarm.get();
    }

    private void sendAlarm(MolluAlarm molluAlarm) {
        if (!molluAlarm.isToday(LocalDateTime.now())) {
            generateAlarm(NOW);
        }

        if (!molluAlarm.isSend()) {
            registerAlarm();
        }
    }

    private void generateAlarm(LocalDate currentDate) {
        final MolluAlarmRange molluAlarmRange = MolluAlarmRange.getInstance();
        final LocalTime sendTime = timePicker.pick(molluAlarmRange.getFrom(), molluAlarmRange.getTo());
        final LocalDateTime molluTime = LocalDateTime.of(currentDate, sendTime);
        final String question = picker.pick(getQuestions());

        molluAlarmRepository.save(new MolluAlarm(molluTime, question, false));
    }

    private List<String> getQuestions() {
        final ClassPathResource resource = new ClassPathResource("questions.txt");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (IOException e) {
            throw new FileException("Questions 파일을 읽어오는데 실패했습니다.");
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    private void schedule() {
        generateAlarm(LocalDate.now());
        registerAlarm();
    }

    private void registerAlarm() {
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
