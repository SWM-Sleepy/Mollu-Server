package sleepy.mollu.server.content.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.member.domain.Member;

public interface ReactionRepository extends JpaRepository<Reaction, String> {

    boolean existsByMemberAndContent(Member member, Content content);
}
