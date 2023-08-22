package sleepy.mollu.server;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.repository.GroupRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Profile({"local", "dev"})
@Service
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final GroupRepository groupRepository;
    private final IdConstructor idConstructor;
    private final MolluAlarmRepository molluAlarmRepository;

    @PostConstruct
    public void init() {

        final Optional<Group> group = groupRepository.findDefaultGroup();
        if (group.isPresent()) return;

        groupRepository.save(Group.builder()
                .id(idConstructor.create())
                .name("디폴트 그룹")
                .introduction("디폴트 그룹입니다.")
                .groupProfileSource("")
                .build());

        final LocalDateTime now = LocalDateTime.now();
        molluAlarmRepository.save(new MolluAlarm(now.minusDays(1), "어제의 질문", true));
        molluAlarmRepository.save(new MolluAlarm(now, "오늘의 질문", false));
    }
}
