package sleepy.mollu.server.content.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.member.domain.Member;

@Entity
@DiscriminatorValue("COMMENT")
@Getter
@NoArgsConstructor
public class CommentReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;


    @Builder
    public CommentReport(String reason, Member member, Comment comment) {
        super(reason, member);
        this.comment = comment;
    }
}
