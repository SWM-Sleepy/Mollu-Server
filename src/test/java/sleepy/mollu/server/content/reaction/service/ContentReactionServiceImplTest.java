package sleepy.mollu.server.content.reaction.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.common.exception.ConflictException;
import sleepy.mollu.server.common.exception.UnAuthorizedException;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.reaction.controller.dto.SearchReactionResponse;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.content.reaction.repository.ReactionRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.emoji.domain.EmojiType;
import sleepy.mollu.server.member.emoji.exception.EmojiNotFoundException;
import sleepy.mollu.server.member.exception.MemberContentUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.member.service.AuthorizationService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ContentReactionServiceImplTest {

    @InjectMocks
    private ContentReactionServiceImpl contentReactionService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @Mock
    private IdConstructor idConstructor;

    @Mock
    private AuthorizationService authorizationService;

    @Nested
    @DisplayName("[컨텐츠 반응 추가 서비스 호출시] ")
    class ContentReactionServiceImplTest0 {

        final String memberId = "memberId";
        final String contentId = "contentId";
        final String type = "emoticon1";

        final Member member = mock(Member.class);
        final Member member2 = mock(Member.class);
        final Content content = mock(Content.class);
        final Reaction reaction = mock(Reaction.class);

        @Test
        @DisplayName("멤버가 컨텐츠에 반응할 권한이 없다면, UnAuthorized 예외를 던진다.")
        void ContentReactionServiceImplTest2() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            willThrow(MemberContentUnAuthorizedException.class).given(authorizationService).authorizeMemberForContent(member, content);

            // when & then
            assertThatThrownBy(() -> contentReactionService.createReaction(memberId, contentId, type))
                    .isInstanceOf(UnAuthorizedException.class);
        }

        @Test
        @DisplayName("멤버가 이미 컨텐츠에 반응했다면, Conflict 예외를 던진다.")
        void ContentReactionServiceImplTest3() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(reactionRepository.existsByMemberAndContent(member, content)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> contentReactionService.createReaction(memberId, contentId, type))
                    .isInstanceOf(ConflictException.class);
        }

        @Test
        @DisplayName("해당 타입의 이모티콘을 등록하지 않았으면, NotFound 예외를 던진다")
        void ContentReactionServiceImplTest4() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(reactionRepository.existsByMemberAndContent(member, content)).willReturn(false);
            given(member.hasEmojiFrom(EmojiType.from(type))).willReturn(false);

            // when & then
            assertThatThrownBy(() -> contentReactionService.createReaction(memberId, contentId, type))
                    .isInstanceOf(EmojiNotFoundException.class);
        }

        @Test
        @DisplayName("성공적으로 컨텐츠에 반응을 추가한다.")
        void ContentReactionServiceImplTest5() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(reactionRepository.existsByMemberAndContent(member, content)).willReturn(false);
            given(member.hasEmojiFrom(EmojiType.from(type))).willReturn(true);
            given(idConstructor.create()).willReturn("reactionId");
            given(reactionRepository.save(any(Reaction.class))).willReturn(reaction);
            given(reaction.getId()).willReturn("reactionId");

            // when
            contentReactionService.createReaction(memberId, contentId, type);

            // then
            then(reactionRepository).should(times(1)).save(any(Reaction.class));
        }
    }

    @Nested
    @DisplayName("[컨텐츠 반응 조회 서비스 호출시] ")
    class ContentReactionServiceImplTest1 {

        final String memberId = "memberId";
        final String contentId = "contentId";

        final Member member = mock(Member.class);
        final Content content = mock(Content.class);
        final Reaction reaction1 = mock(Reaction.class);
        final Reaction reaction2 = mock(Reaction.class);

        @Test
        @DisplayName("내가 반응을 하지 않았다면, 다른 사람들의 반응만 응답한다.")
        void ContentReactionServiceImplTest0() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(reactionRepository.findByMemberAndContent(member, content)).willReturn(Optional.empty());
            given(reactionRepository.findAllByContentExcludesMember(content, member)).willReturn(List.of(reaction1, reaction2));
            given(reaction1.getId()).willReturn("reaction1");
            given(reaction2.getId()).willReturn("reaction2");

            // when
            final SearchReactionResponse response = contentReactionService.searchReaction(memberId, contentId);

            // then
            assertThat(response.reactions()).extracting("reactionId").containsExactly(reaction1.getId(), reaction2.getId());
        }

        @Test
        @DisplayName("내가 반응을 했다면, 내 반응은 첫 번째로 응답한다.")
        void ContentReactionServiceImplTest1() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(reactionRepository.findByMemberAndContent(member, content)).willReturn(Optional.of(reaction1));
            given(reactionRepository.findAllByContentExcludesMember(content, member)).willReturn(List.of(reaction2));
            given(reaction1.getId()).willReturn("reaction1");
            given(reaction2.getId()).willReturn("reaction2");

            // when
            final SearchReactionResponse response = contentReactionService.searchReaction(memberId, contentId);

            // then
            assertThat(response.reactions()).extracting("reactionId").containsExactly(reaction1.getId(), reaction2.getId());
        }
    }
}