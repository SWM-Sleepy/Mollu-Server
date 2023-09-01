package sleepy.mollu.server.member.emoji.service;

import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.member.emoji.controller.dto.SearchMyEmojiResponse;

public interface MemberEmojiService {

    void createMyEmoji(String memberId, String emojiType, MultipartFile emojiFile);

    SearchMyEmojiResponse searchMyEmoji(String memberId);
}
