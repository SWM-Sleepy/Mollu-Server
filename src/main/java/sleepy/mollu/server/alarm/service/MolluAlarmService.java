package sleepy.mollu.server.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.exception.MolluAlarmNotFoundException;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MolluAlarmService implements AlarmService {

    private final MolluAlarmRepository molluAlarmRepository;
    private final MemberRepository memberRepository;
    private final AlarmClient alarmClient;

    @Transactional
    @Override
    public void sendAlarm() {
        final MolluAlarm molluAlarm = getMolluAlarm();
        sendAlarmToAllowedMembers(molluAlarm.getQuestion());
        molluAlarm.updateSend();
    }

    private MolluAlarm getMolluAlarm() {
        return molluAlarmRepository.findTop()
                .orElseThrow(() -> new MolluAlarmNotFoundException("`MolluAlarm`이 존재하지 않습니다."));
    }

    private void sendAlarmToAllowedMembers(String question) {
        final List<Member> alarmAllowedMembers = memberRepository.findAllByMolluAlarmAllowed();
        final List<String> phoneTokens = alarmAllowedMembers.stream()
                .map(Member::getPhoneToken)
                .filter(Objects::nonNull)
                .toList();

        alarmClient.send(phoneTokens, question);
    }
}
