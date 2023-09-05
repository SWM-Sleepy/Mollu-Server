package sleepy.mollu.server.member.emoji.domain;

import jakarta.persistence.*;
import lombok.Getter;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

@Entity
@Getter
public class Emoji extends BaseEntity {

    private static final String EMPTY_EMOJI_SOURCE = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emoji_id")
    private long id;

    private String emoji1;
    private String emoji2;
    private String emoji3;
    private String emoji4;
    private String emoji5;

    @OneToOne(mappedBy = "emoji", fetch = FetchType.LAZY)
    private Member member;

    public Emoji() {
        this.emoji1 = EMPTY_EMOJI_SOURCE;
        this.emoji2 = EMPTY_EMOJI_SOURCE;
        this.emoji3 = EMPTY_EMOJI_SOURCE;
        this.emoji4 = EMPTY_EMOJI_SOURCE;
        this.emoji5 = EMPTY_EMOJI_SOURCE;
    }

    public void assignMember(Member member) {
        this.member = member;
    }

    public List<String> getEmojis() {
        return List.of(emoji1, emoji2, emoji3, emoji4, emoji5);
    }

    public void update(EmojiType emojiType, String emojiSource) {
        updateEmojiBy(emojiType, emojiSource);
    }

    public void delete(EmojiType emojiType) {
        updateEmojiBy(emojiType, EMPTY_EMOJI_SOURCE);
    }

    private void updateEmojiBy(EmojiType emojiType, String emojiSource) {
        switch (emojiType) {
            case EMOTICON1 -> this.emoji1 = emojiSource;
            case EMOTICON2 -> this.emoji2 = emojiSource;
            case EMOTICON3 -> this.emoji3 = emojiSource;
            case EMOTICON4 -> this.emoji4 = emojiSource;
            case EMOTICON5 -> this.emoji5 = emojiSource;
        }
    }

    public String getSourceFrom(EmojiType emojiType) {
        if (emojiType == EmojiType.EMOTICON1) return this.emoji1;
        if (emojiType == EmojiType.EMOTICON2) return this.emoji2;
        if (emojiType == EmojiType.EMOTICON3) return this.emoji3;
        if (emojiType == EmojiType.EMOTICON4) return this.emoji4;
        return this.emoji5;
    }

    public boolean hasFrom(EmojiType emojiType) {
        final String source = getSourceFrom(emojiType);
        return !source.equals(EMPTY_EMOJI_SOURCE);
    }
}
