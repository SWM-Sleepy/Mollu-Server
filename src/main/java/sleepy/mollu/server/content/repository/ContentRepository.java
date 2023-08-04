package sleepy.mollu.server.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, String> {

    Optional<Content> findTopByMemberOrderByCreatedAtDesc(Member member);
}