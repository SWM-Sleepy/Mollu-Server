package sleepy.mollu.server.admin.controller.model;

import java.time.LocalDateTime;

public record CommentReportModel(String commentId, int reportNum, String message, LocalDateTime createdAt) {
}
