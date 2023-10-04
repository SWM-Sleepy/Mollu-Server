package sleepy.mollu.server.content.comment.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record SearchCommentResponse(List<CommentResponse> comments) {

    public record CommentResponse(String commentId, String comment, String memberId, String userName,
                                  String profileSource, LocalDateTime createdAt) {
    }
}
