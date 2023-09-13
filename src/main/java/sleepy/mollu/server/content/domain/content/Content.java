package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Content extends BaseEntity {

    @Id
    @Column(name = "content_id")
    private String id;

    @Embedded
    private Location location;

    @Embedded
    private Question question;

    @Embedded
    private ContentTag contentTag;

    @Embedded
    private ContentTime contentTime;

    @Embedded
    private ContentSource contentSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Content(String id, String location, String contentTag, String question, ContentTime contentTime, ContentSource contentSource, Member member) {
        this.id = id;
        this.location = new Location(location);
        this.contentTag = new ContentTag(contentTag);
        this.question = new Question(question);
        this.contentTime = contentTime;
        this.contentSource = contentSource;
        this.member = member;
    }

    public String getLocation() {
        return location.getValue();
    }

    public String getContentTag() {
        return contentTag.getValue();
    }

    public String getQuestion() {
        if (this.question == null) {
            return "";
        }
        return this.question.getValue();
    }

    public String getFrontContentSource() {
        return contentSource.getFrontSource();
    }

    public String getBackContentSource() {
        return contentSource.getBackSource();
    }

    public LocalDateTime getMolluDateTime() {
        return contentTime.getMolluDateTime();
    }

    public LocalDateTime getUploadDateTime() {
        return contentTime.getUploadDateTime();
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

    public String getThumbnailFrontSource() {
        return this.contentSource.getThumbnailFrontSource();
    }
}

