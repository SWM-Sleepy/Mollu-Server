package sleepy.mollu.server.content.reaction.service;

import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionResponse;

public interface ContentReactionService {

    String createReaction(String memberId, String contentId, String type);

    SearchReactionResponse searchReaction(String memberId, String contentId);

    void deleteReaction(String memberId, String contentId, String reactionId);
}
