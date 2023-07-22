package sleepy.mollu.server.content.report.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.content.domain.content.Content;

@Entity
@Getter
@NoArgsConstructor
public class ContentReport extends Report {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Builder
    public ContentReport(String reason, Content content) {
        super(reason);
        this.content = content;
    }

    public void assignContent(Content content) {
        this.content = content;
    }
}
