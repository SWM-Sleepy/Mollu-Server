package sleepy.mollu.server.content.contentgroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;

public interface ContentGroupRepository extends JpaRepository<ContentGroup, String>, CustomContentGroupRepository {

}
