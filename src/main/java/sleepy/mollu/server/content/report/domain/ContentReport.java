package sleepy.mollu.server.content.report.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class ContentReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Builder
    public ContentReport(String reason, @NonNull Member member, @NonNull Content content) {
        super(reason, member);
        setContent(content);
    }

    private void setContent(Content content) {
        this.content = content;
        content.addContentReport(this);
    }
}
