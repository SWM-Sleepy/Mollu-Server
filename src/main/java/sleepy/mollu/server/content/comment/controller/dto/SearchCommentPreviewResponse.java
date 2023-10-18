package sleepy.mollu.server.content.comment.controller.dto;

import java.time.LocalDateTime;

public record SearchCommentPreviewResponse(Long commentNum, CommentResponse comment) {

    public record CommentResponse(String commentId, String comment, String molluId, String userName,
                                  String profileSource, LocalDateTime createdAt) {
    }
}
