package sleepy.mollu.server.content.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, String> {

    Optional<Content> findTopByMemberOrderByCreatedAtDesc(Member member);

    @Query("select c from Content c where c.member = :member and c.contentTime.uploadDateTime between :from and :to " +
            "order by c.contentTime.uploadDateTime desc")
    List<Content> findAllByMemberAndDate(@Param("member") Member member,
                                         @Param("from") LocalDateTime from,
                                         @Param("to") LocalDateTime to);

    @Query("select c from Content c where c.member = :member order by c.contentTime.uploadDateTime desc")
    List<Content> findAllByMember(@Param("member") Member member);
}