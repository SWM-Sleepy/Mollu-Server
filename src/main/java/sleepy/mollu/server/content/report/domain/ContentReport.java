package sleepy.mollu.server.content.report.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

@Entity
@DiscriminatorValue("CONTENT")
@Getter
@NoArgsConstructor
public class ContentReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;

    @Builder
    public ContentReport(String reason, Member member, Content content) {
        super(reason, member);
        this.content = content;
    }
}
