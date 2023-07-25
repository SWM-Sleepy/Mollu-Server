package sleepy.mollu.server.content.contentgroup.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.group.domain.group.Group;

@Entity
@Getter
@NoArgsConstructor
public class ContentGroup {

    @Id
    @Column(name = "content_group_id")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public ContentGroup(@NonNull String id, @NonNull Group group) {
        this.id = id;
        this.group = group;
    }

    public void assignContent(Content content) {
        this.content = content;
    }
}
