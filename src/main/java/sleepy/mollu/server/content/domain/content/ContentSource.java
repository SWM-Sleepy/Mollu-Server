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
    private FileSource frontContentSource;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "back_content_source"))
    private FileSource backContentSource;

    public static ContentSource of(String frontContentSource, String backContentSource) {
        return new ContentSource(new FileSource(frontContentSource), new FileSource(backContentSource));
    }

    public String getFrontContentSource() {
        return frontContentSource.getValue();
    }

    public String getBackContentSource() {
        return backContentSource.getValue();
    }
}
