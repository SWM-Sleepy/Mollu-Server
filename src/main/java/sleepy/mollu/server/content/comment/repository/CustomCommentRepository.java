package sleepy.mollu.server.content.comment.repository;

import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.domain.content.Content;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {

    Long countByContent(Content content, List<Comment> comments);
    Optional<Comment> findTop(Content content, List<Comment> comments);
}
