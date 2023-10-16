package sleepy.mollu.server.content.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.domain.content.Content;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, String>, CustomCommentRepository {

    @Query("select c from Comment c join fetch c.member where c.content = :content order by c.createdAt asc")
    List<Comment> findAllWithMemberByContent(@Param("content") Content content);
}
