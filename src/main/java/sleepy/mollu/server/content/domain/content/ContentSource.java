package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.FileSource;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ContentSource {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "front_content_source"))
    private FileSource frontSource;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "back_content_source"))
    private FileSource backSource;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "thumbnail_front_content_source"))
    private FileSource thumbnailFrontSource;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "thumbnail_back_content_source"))
    private FileSource thumbnailBackSource;

    public static ContentSource of(String frontSource, String backSource, String thumbnailFrontSource, String thumbnailBackSource) {
        return new ContentSource(
                new FileSource(frontSource),
                new FileSource(backSource),
                new FileSource(thumbnailFrontSource),
                new FileSource(thumbnailBackSource));
    }

    public String getFrontSource() {
        return this.frontSource.getValue();
    }

    public String getBackSource() {
        return this.backSource.getValue();
    }

    public String getThumbnailFrontSource() {
        if (this.thumbnailFrontSource == null) {
            return this.frontSource.getValue();
        }

        return this.thumbnailFrontSource.getValue();
    }
}
