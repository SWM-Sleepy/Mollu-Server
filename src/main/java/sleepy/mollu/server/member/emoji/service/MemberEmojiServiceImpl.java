package sleepy.mollu.server.member.emoji.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberEmojiServiceImpl implements MemberEmojiService {

    @Override
    public void createMyEmoji(String memberId, String emoji, MultipartFile emojiFile) {

    }
}
