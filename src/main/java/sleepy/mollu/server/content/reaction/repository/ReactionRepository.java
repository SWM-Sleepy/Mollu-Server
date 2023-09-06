package sleepy.mollu.server.content.reaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, String> {

    boolean existsByMemberAndContent(Member member, Content content);

    Optional<Reaction> findByMemberAndContent(Member member, Content content);

    @Query("select r from Reaction r where r.content = :content and r.member != :member")
    List<Reaction> findAllByContentExcludesMember(@Param("content") Content content, @Param("member") Member member);
}
