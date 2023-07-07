package sleepy.mollu.server.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.domain.Content;

public interface ContentRepository extends JpaRepository<Content, String> {
}
