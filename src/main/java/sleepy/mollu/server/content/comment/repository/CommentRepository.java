package sleepy.mollu.server.content.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sleepy.mollu.server.content.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, String> {
}
