package sleepy.mollu.server.content.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ContentCommentServiceImpl implements ContentCommentService {

    @Override
    public String createComment(String memberId, String contentId, String comment) {
        return null;
    }
}
