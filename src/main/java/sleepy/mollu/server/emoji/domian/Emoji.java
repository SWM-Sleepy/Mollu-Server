package sleepy.mollu.server.emoji.domian;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class Emoji extends BaseEntity {

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

    public void assignMember(Member member) {
        this.member = member;
    }

    public void update(String emojiType, String emojiSource) {
        switch (emojiType) {
            case "emoticon1" -> this.emoji1 = emojiSource;
            case "emoticon2" -> this.emoji2 = emojiSource;
            case "emoticon3" -> this.emoji3 = emojiSource;
            case "emoticon4" -> this.emoji4 = emojiSource;
            case "emoticon5" -> this.emoji5 = emojiSource;
            default -> throw new IllegalArgumentException("[" + emojiType + "]는 존재하지 않는 이모티콘입니다.");
        }
    }
}
