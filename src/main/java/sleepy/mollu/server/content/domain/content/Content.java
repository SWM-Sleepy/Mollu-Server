package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Content extends BaseEntity {

    @Id
    @Column(name = "content_id")
    private String id;

    @Embedded
    private Location location;

    @Embedded
    private ContentTag contentTag;

    @Embedded
    private ContentTime contentTime;

    @Embedded
    private ContentSource contentSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Content(String id, String location, String contentTag, ContentTime contentTime, ContentSource contentSource, Member member) {
        this.id = id;
        this.location = new Location(location);
        this.contentTag = new ContentTag(contentTag);
        this.contentTime = contentTime;
        this.contentSource = contentSource;
        this.member = member;
    }

    public String getContentTag() {
        return contentTag.getValue();
    }

    public String getLocation() {
        return location.getValue();
    }

    public String getFrontContentSource() {
        return contentSource.getFrontContentSource();
    }

    public String getBackContentSource() {
        return contentSource.getBackContentSource();
    }

    public LocalDateTime getMolluDateTime() {
        return contentTime.getMolluDateTime();
    }

    public boolean isOwner(String memberId) {
        return this.member.isSameId(memberId);
    }

    public boolean isOwner(Member member) {
        return this.member == member;
    }

    public boolean isUploadedBefore(LocalDateTime localDateTime) {
        return contentTime.isUploadedBefore(localDateTime);
    }
}
