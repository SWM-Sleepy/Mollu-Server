package sleepy.mollu.server.member.emoji.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.emoji.domian.Emoji;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberEmojiServiceImpl implements MemberEmojiService {

    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;

    @Transactional
    @Override
    public void createMyEmoji(String memberId, String emojiType, MultipartFile emojiFile) {

        final Member member = getMember(memberId);
        final Emoji emoji = getEmoji(member);
        final ContentFile contentFile = new ImageContentFile(emojiFile);
        final String emojiSource = fileHandler.upload(contentFile);

        emoji.update(emojiType, emojiSource);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 멤버입니다."));
    }

    private Emoji getEmoji(Member member) {
        if (!member.hasEmoji()) {
            member.createEmoji();
        }

        return member.getEmoji();
    }
}
