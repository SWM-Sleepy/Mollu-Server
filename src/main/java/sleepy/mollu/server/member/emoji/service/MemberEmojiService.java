package sleepy.mollu.server.member.emoji.service;

import org.springframework.web.multipart.MultipartFile;

public interface MemberEmojiService {

    void createMyEmoji(String memberId, String emojiType, MultipartFile emojiFile);
}
