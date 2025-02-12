package sleepy.mollu.server.content.contentgroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.domain.content.Content;

import java.util.List;

public interface ContentGroupRepository extends JpaRepository<ContentGroup, String>, CustomContentGroupRepository {

    List<ContentGroup> findAllByContent(Content content);
}
