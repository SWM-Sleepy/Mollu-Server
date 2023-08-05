package sleepy.mollu.server.alarm.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.alarm.admin.dto.MolluRangeResponse;
import sleepy.mollu.server.alarm.admin.dto.MolluTimeResponse;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.domain.MolluAlarmRange;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.alarm.service.AlarmService;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AdminAlarmServiceImpl implements AdminAlarmService {

    private final MolluAlarmRepository molluAlarmRepository;
    private final AlarmService alarmService;

    @Override
    public MolluRangeResponse searchMolluAlarmRange() {

        final MolluAlarmRange range = MolluAlarmRange.getInstance();
        return new MolluRangeResponse(range.getFrom(), range.getTo());
    }

    @Override
    public void updateMolluAlarmRange(LocalTime from, LocalTime to) {

        final MolluAlarmRange range = MolluAlarmRange.getInstance();
        range.update(from, to);
    }

    @Override
    public List<MolluTimeResponse> searchMolluTimes() {
        final List<MolluAlarm> molluAlarms = molluAlarmRepository.findAllByOrderByIdDesc();

        return molluAlarms.stream()
                .map(molluAlarm -> new MolluTimeResponse(molluAlarm.getId(), molluAlarm.getMolluTime(), molluAlarm.getCreatedAt()))
                .toList();
    }

    @Override
    public void sendMolluAlarm() {
        alarmService.sendAlarm();
    }
}
