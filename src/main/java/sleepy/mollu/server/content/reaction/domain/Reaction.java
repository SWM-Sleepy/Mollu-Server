package sleepy.mollu.server.content.reaction.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.FileSource;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.emoji.domain.EmojiType;

@Entity
@Getter
@NoArgsConstructor
public class Reaction {

    @Id
    @Column(name = "reaction_id")
    private String id;

    @Enumerated(EnumType.STRING)
    private EmojiType type;

    @Embedded
    private FileSource reactionSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Builder
    public Reaction(String id, EmojiType type, FileSource reactionSource, Member member, Content content) {
        this.id = id;
        this.type = type;
        this.reactionSource = reactionSource;
        this.member = member;
        this.content = content;
    }
}
