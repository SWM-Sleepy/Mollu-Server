package sleepy.mollu.server.content.reaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class ContentReactionServiceImpl implements ContentReactionService {

    @Override
    public void createReaction(String memberId, String contentId, String type) {

    }
}
