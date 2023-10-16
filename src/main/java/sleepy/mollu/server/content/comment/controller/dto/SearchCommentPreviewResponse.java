package sleepy.mollu.server.content.comment.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchCommentPreviewResponse(Long commentNum, CommentResponse comment) {

    public record CommentResponse(String commentId, String comment, String molluId, String userName,
                                  String profileSource, LocalDateTime createdAt) {
    }
}
