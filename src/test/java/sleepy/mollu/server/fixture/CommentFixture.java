package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

public class CommentFixture {

    private static final String DEFAULT_MESSAGE = "댓글";

    public static Comment create(String commentId, Member member, Content content) {
        return Comment.builder()
                .id(commentId)
                .message(DEFAULT_MESSAGE)
                .member(member)
                .content(content)
                .build();
    }
}
