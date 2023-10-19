package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.member.domain.Member;

public class CommentReportFixture {

    private static final String DEFAULT_REASON = "이유";

    public static CommentReport create(Member member, Comment comment) {
        return CommentReport.builder()
                .reason(DEFAULT_REASON)
                .member(member)
                .comment(comment)
                .build();
    }
}
