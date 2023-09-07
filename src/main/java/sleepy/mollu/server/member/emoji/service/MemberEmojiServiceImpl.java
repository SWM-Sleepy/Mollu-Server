package sleepy.mollu.server.member.emoji.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.emoji.controller.dto.SearchMyEmojiResponse;
import sleepy.mollu.server.member.emoji.domain.Emoji;
import sleepy.mollu.server.member.emoji.domain.EmojiType;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;

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
        createEmojiIfNotExists(member);
        final String emojiSource = uploadEmoji(emojiFile);

        member.updateEmoji(EmojiType.from(emojiType), emojiSource);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 멤버입니다."));
    }

    private void createEmojiIfNotExists(Member member) {
        if (!member.hasEmoji()) {
            member.createEmoji();
        }
    }

    private String uploadEmoji(MultipartFile emojiFile) {
        final ContentFile contentFile = new ImageContentFile(emojiFile, ContentType.EMOJIS);
        return fileHandler.upload(contentFile);
    }

    @Override
    public SearchMyEmojiResponse searchMyEmoji(String memberId) {
        final Member member = getMember(memberId);
        final Emoji emoji = member.getEmoji();

        return getMyEmojiResponse(emoji);
    }

    private SearchMyEmojiResponse getMyEmojiResponse(Emoji emoji) {
        if (emoji == null) {
            final List<String> defaultEmojis = List.of("", "", "", "", "");
            return new SearchMyEmojiResponse(defaultEmojis);
        }

        return new SearchMyEmojiResponse(emoji.getEmojis());
    }

    @Transactional
    @Override
    public void deleteMyEmoji(String memberId, String emojiType) {
        final Member member = getMember(memberId);
        member.deleteEmoji(EmojiType.from(emojiType));
    }
}
