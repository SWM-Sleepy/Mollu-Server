package sleepy.mollu.server.content.reaction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.reaction.repository.ReactionRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ContentReactionServiceImpl implements ContentReactionService {

    private final ReactionRepository reactionRepository;

    @Transactional
    @Override
    public void createReaction(String memberId, String contentId, String type) {

    }
}
