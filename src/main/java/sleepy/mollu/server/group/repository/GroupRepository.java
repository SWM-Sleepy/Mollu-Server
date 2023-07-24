package sleepy.mollu.server.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.group.domain.group.Group;

public interface GroupRepository extends JpaRepository<Group, String> {
}
