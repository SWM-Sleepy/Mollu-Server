package sleepy.mollu.server.content.contentgroup.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.group.domain.group.Group;

import java.util.List;

public interface ContentGroupRepository extends JpaRepository<ContentGroup, String> {

    @Query("select cg from ContentGroup cg where cg.group in :groups")
    Page<ContentGroup> findAllByGroups(List<Group> groups, Pageable pageable);

    @Query("select cg from ContentGroup cg join fetch cg.content where cg in :contentGroups")
    List<ContentGroup> findAllWithContentByContentGroups(List<ContentGroup> contentGroups);
}
