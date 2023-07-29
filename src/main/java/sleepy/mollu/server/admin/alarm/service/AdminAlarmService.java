package sleepy.mollu.server.admin.alarm.service;

import sleepy.mollu.server.admin.alarm.dto.MolluRangeResponse;
import sleepy.mollu.server.admin.alarm.dto.MolluTimeResponse;

import java.time.LocalTime;
import java.util.List;

public interface AdminAlarmService {

    MolluRangeResponse searchMolluAlarmRange();

    void updateMolluAlarmRange(LocalTime from, LocalTime to);

    List<MolluTimeResponse> searchMolluTimes();
}
