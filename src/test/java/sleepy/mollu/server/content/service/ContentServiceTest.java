package sleepy.mollu.server.content.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.content.exception.ContentNotFoundException;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

    @InjectMocks
    private ContentServiceImpl contentService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private IdConstructor idConstructor;

    @Mock
    private FileHandler fileHandler;


    @Nested
    @DisplayName("[컨텐츠 삭제 서비스 호출시] ")
    class DeleteContentTest {

        final String memberId = "memberId";
        final String contentId = "contentId";

        @Test
        @DisplayName("컨텐츠가 존재하지 않는다면 NotFound 예외를 던진다")
        void deleteContentTest1() {
            // given
            given(contentRepository.findByIdOrElseThrow(contentId)).willThrow(ContentNotFoundException.class);

            // when & then
            assertThatThrownBy(() -> contentService.deleteContent(memberId, contentId))
                    .isInstanceOf(ContentNotFoundException.class);
        }

        @Test
        @DisplayName("본인이 올린 컨텐츠가 아니라면 Authorization 예외를 던진다")
        void deleteContentTest2() {
            // given
            final Content content = mock(Content.class);

            given(content.isOwner(memberId)).willReturn(false);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);

            // when & then
            assertThatThrownBy(() -> contentService.deleteContent(memberId, contentId))
                    .isInstanceOf(MemberUnAuthorizedException.class);
        }

        @Test
        @DisplayName("본인이 올린 컨텐츠라면 성공적으로 삭제한다.")
        void deleteContentTest3() {
            // given
            final Content content = mock(Content.class);

            given(content.isOwner(memberId)).willReturn(true);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);

            // when
            contentService.deleteContent(memberId, contentId);

            // then
            then(contentRepository).should().delete(content);
        }
    }
}