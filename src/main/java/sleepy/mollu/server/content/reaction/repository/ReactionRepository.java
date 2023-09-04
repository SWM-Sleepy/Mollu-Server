package sleepy.mollu.server.content.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.reaction.domain.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, String> {
}
