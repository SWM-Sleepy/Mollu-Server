package sleepy.mollu.server;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final GroupRepository groupRepository;
    private final IdConstructor idConstructor;

    @PostConstruct
    public void init() {
        groupRepository.save(Group.builder()
                .id(idConstructor.create())
                .name("디폴트 그룹")
                .introduction("디폴트 그룹입니다.")
                .groupProfileSource("")
                .build());
    }
}
