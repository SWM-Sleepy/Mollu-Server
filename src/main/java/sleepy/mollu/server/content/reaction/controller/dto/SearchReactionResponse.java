package sleepy.mollu.server.content.reaction.controller.dto;

import java.util.List;

public record SearchReactionResponse(List<ReactionResponse> reactions) {

    public record ReactionResponse(String reactionId, String emoji, String userName) {
    }
}
