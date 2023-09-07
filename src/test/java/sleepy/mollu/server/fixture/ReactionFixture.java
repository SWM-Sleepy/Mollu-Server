package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.emoji.domain.EmojiType;

public class ReactionFixture {

    private static final String DEFAULT_REACTION_SOURCE = "https://mollu.com/emoji/1.png";

    public static Reaction create(String reactionId, Member member, Content content) {
        return Reaction.builder()
                .id(reactionId)
                .type(EmojiType.EMOTICON1)
                .reactionSource(DEFAULT_REACTION_SOURCE)
                .member(member)
                .content(content)
                .build();
    }
}
