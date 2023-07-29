package sleepy.mollu.server.admin.alarm.service;

import org.springframework.stereotype.Service;
import sleepy.mollu.server.admin.alarm.dto.MolluRangeResponse;
import sleepy.mollu.server.alarm.domain.MolluAlarmRange;

import java.time.LocalTime;

@Service
public class AdminAlarmServiceImpl implements AdminAlarmService {

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
}
