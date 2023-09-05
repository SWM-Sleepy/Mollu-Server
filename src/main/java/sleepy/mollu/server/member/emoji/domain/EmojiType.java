package sleepy.mollu.server.member.emoji.domain;

public enum EmojiType {
    EMOTICON1, EMOTICON2, EMOTICON3, EMOTICON4, EMOTICON5;

    public static EmojiType from(String type) {
        try {
            return EmojiType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new IllegalArgumentException("[" + type + "]은 올바르지 않은 이모티콘입니다.");
        }
    }
}
