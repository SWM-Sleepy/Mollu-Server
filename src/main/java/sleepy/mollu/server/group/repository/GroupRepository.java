package sleepy.mollu.server.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sleepy.mollu.server.group.domain.group.Group;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, String> {

    @Query("select g from Group g")
    Optional<Group> findDefaultGroup();
}
