package sleepy.mollu.server.content.reaction.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.common.domain.FileSource;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.emoji.domain.EmojiType;

@Entity
@Getter
@NoArgsConstructor
public class Reaction extends BaseEntity {

    @Id
    @Column(name = "reaction_id")
    private String id;

    @Enumerated(EnumType.STRING)
    private EmojiType type;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "reaction_source"))
    private FileSource reactionSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Content content;

    @Builder
    public Reaction(String id, EmojiType type, String reactionSource, Member member, Content content) {
        this.id = id;
        this.type = type;
        this.reactionSource = new FileSource(reactionSource);
        this.member = member;
        this.content = content;
    }

    public String getReactionSource() {
        return this.reactionSource.getValue();
    }

    public String getMemberName() {
        return this.member.getName();
    }

    public boolean isOwner(Member member) {
        return this.member.equals(member);
    }
}
