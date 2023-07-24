package sleepy.mollu.server.content.domain.content;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.common.domain.FileSource;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Content extends BaseEntity {

    @Id
    @Column(name = "content_id")
    private String id;

    @Embedded
    private ContentTag contentTag;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "front_content_source"))
    private FileSource frontContentSource;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "back_content_source"))
    private FileSource backContentSource;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "content")
    private List<ContentReport> reports = new ArrayList<>();


    @Builder
    public Content(String id, String contentTag, String frontContentSource, String backContentSource, String location, Member member) {
        this.id = id;
        this.contentTag = new ContentTag(contentTag);
        this.frontContentSource = new FileSource(frontContentSource);
        this.backContentSource = new FileSource(backContentSource);
        this.location = new Location(location);
        this.member = member;
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
        this.frontContentSource = new FileSource(frontContentSource);
        this.backContentSource = new FileSource(backContentSource);
    }

    public boolean isOwner(String memberId) {
        return this.member.isSameId(memberId);
    }

    public boolean isOwner(Member member) {
        return this.member == member;
    }

    public void addContentReport(ContentReport contentReport) {
        this.reports.add(contentReport);
    }
}
