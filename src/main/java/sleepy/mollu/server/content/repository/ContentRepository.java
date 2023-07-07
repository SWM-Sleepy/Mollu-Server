package sleepy.mollu.server.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.domain.Content;

public interface ContentRepository extends JpaRepository<Content, String> {

    Page<Content> findAll(Pageable pageable);
}
