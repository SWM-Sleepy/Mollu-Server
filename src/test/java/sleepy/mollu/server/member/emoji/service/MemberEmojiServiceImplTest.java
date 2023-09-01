package sleepy.mollu.server.member.emoji.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.emoji.domian.Emoji;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberEmojiServiceImplTest {

    @InjectMocks
    private MemberEmojiServiceImpl memberEmojiService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FileHandler fileHandler;

    @Nested
    @DisplayName("[내 이모티콘 등록 메서드 호출시] ")
    class CreateMyEmoji {

        final String memberId = "memberId";
        final String emojiType = "emoticon1";
        final MultipartFile mockFile = new MockMultipartFile("photo", "file.png", "image/png", "Spring Framework".getBytes());
        final String emojiSource = "emojiSource";

        final Member member = mock(Member.class);
        final Emoji emoji = mock(Emoji.class);

        @Test
        @DisplayName("멤버가 존재하지 않으면, NotFound 예외를 던진다")
        void CreateMyEmoji0() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> memberEmojiService.createMyEmoji(memberId, emojiType, mockFile))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("이모티콘이 존재하지 않으면, 새로운 이모티콘을 생성한다")
        void CreateMyEmoji1() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(member.hasEmoji()).willReturn(false);
            given(member.getEmoji()).willReturn(emoji);

            // when
            memberEmojiService.createMyEmoji(memberId, emojiType, mockFile);

            // then
            then(member).should(times(1)).createEmoji();
        }

        @Test
        @DisplayName("이모티콘이 존재하면, 새로운 이모티콘을 생성하지 않는다")
        void CreateMyEmoji2() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(member.hasEmoji()).willReturn(true);
            given(member.getEmoji()).willReturn(emoji);

            // when
            memberEmojiService.createMyEmoji(memberId, emojiType, mockFile);

            // then
            then(member).should(times(0)).createEmoji();
        }

        @Test
        @DisplayName("이모티콘 이미지를 외부 저장소에 저장하고, 이모티콘 소스 정보를 업데이트한다")
        void CreateMyEmoji3() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(member.getEmoji()).willReturn(emoji);
            given(fileHandler.upload(any(ContentFile.class))).willReturn(emojiSource);

            // when
            memberEmojiService.createMyEmoji(memberId, emojiType, mockFile);

            // then
            then(fileHandler).should(times(1)).upload(any(ContentFile.class));
            then(emoji).should(times(1)).update(emojiType, emojiSource);
        }
    }
}