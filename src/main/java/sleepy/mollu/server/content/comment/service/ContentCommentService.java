package sleepy.mollu.server.content.comment.service;

import sleepy.mollu.server.content.comment.controller.dto.SearchCommentPreviewResponse;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentResponse;

public interface ContentCommentService {

    String createComment(String memberId, String contentId, String comment);

    SearchCommentResponse searchComment(String memberId, String contentId);

    void deleteComment(String memberId, String contentId, String commentId);

    SearchCommentPreviewResponse searchCommentPreview(String memberId, String contentId);
}
