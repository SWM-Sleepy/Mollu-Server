package sleepy.mollu.server.alarm.admin.service;

import sleepy.mollu.server.alarm.admin.dto.MolluRangeResponse;
import sleepy.mollu.server.alarm.admin.dto.MolluTimeResponse;

import java.time.LocalTime;
import java.util.List;

public interface AdminAlarmService {

    MolluRangeResponse searchMolluAlarmRange();

    void updateMolluAlarmRange(LocalTime from, LocalTime to);

    List<MolluTimeResponse> searchMolluTimes();

    void sendMolluAlarm();
}
