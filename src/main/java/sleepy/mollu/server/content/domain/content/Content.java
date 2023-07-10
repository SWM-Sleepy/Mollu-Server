package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
public class Content extends BaseEntity {

    @Id
    private String id;

    @Embedded
    private ContentTag contentTag;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "front_content_source"))
    private ContentSource frontContentSource;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "back_content_source"))
    private ContentSource backContentSource;

    @Embedded
    private Location location;

    @Builder
    public Content(String id, String contentTag, String frontContentSource, String backContentSource, String location) {
        this.id = id;
        this.contentTag = new ContentTag(contentTag);
        this.frontContentSource = new ContentSource(frontContentSource);
        this.backContentSource = new ContentSource(backContentSource);
        this.location = new Location(location);
    }

    public String getContentTag() {
        return contentTag.getValue();
    }

    public String getFrontContentSource() {
        return frontContentSource.getValue();
    }

    public String getBackContentSource() {
        return backContentSource.getValue();
    }

    public String getLocation() {
        return location.getValue();
    }

    public void updateUrl(String frontContentSource, String backContentSource) {
        this.frontContentSource = new ContentSource(frontContentSource);
        this.backContentSource = new ContentSource(backContentSource);
    }
}