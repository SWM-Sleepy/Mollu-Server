package sleepy.mollu.server.content.comment.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "comment_id")
    private String id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "message"))
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parent;

    @Builder
    public Comment(String id, String message, Member member, Content content) {
        this.id = id;
        this.message = new Message(message);
        this.member = member;
        this.content = content;
    }

    public String getMessage() {
        return this.message.getValue();
    }

    public String getMemberId() {
        return this.member.getId();
    }

    public String getMemberName() {
        return this.member.getName();
    }

    public String getMemberProfileSource() {
        return this.member.getProfileSource();
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }

    public boolean belongsTo(Content content) {
        return this.content.equals(content);
    }
}
