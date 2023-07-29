package sleepy.mollu.server.admin.alarm.service;

import sleepy.mollu.server.admin.alarm.dto.MolluRangeResponse;

import java.time.LocalTime;

public interface AdminAlarmService {

    MolluRangeResponse searchMolluAlarmRange();

    void updateMolluAlarmRange(LocalTime from, LocalTime to);
}
