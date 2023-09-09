package sleepy.mollu.server.content.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.comment.repository.CommentRepository;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.exception.ContentNotFoundException;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ContentCommentServiceImplTest {

    @InjectMocks
    private ContentCommentServiceImpl contentCommentService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private ContentGroupRepository contentGroupRepository;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private IdConstructor idConstructor;

    @Nested
    @DisplayName("[댓글 등록 서비스 호출시] ")
    class ContentCommentServiceImplTest0 {

        final String memberId = "memberId";
        final String contentId = "contentId";
        final String message = "comment";
        final String savedCommentId = "commentId";

        final Member member = mock(Member.class);
        final Member member2 = mock(Member.class);
        final Content content = mock(Content.class);
        final GroupMember groupMember = mock(GroupMember.class);
        final Comment comment = mock(Comment.class);

        @Test
        @DisplayName("멤버가 없으면, NotFound 예외를 던진다.")
        void ContentCommentServiceImplTest0() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> contentCommentService.createComment(memberId, contentId, message))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("컨텐츠가 없으면, NotFound 예외를 던진다.")
        void ContentCommentServiceImplTest1() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findById(contentId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> contentCommentService.createComment(memberId, contentId, message))
                    .isInstanceOf(ContentNotFoundException.class);
        }

        @Test
        @DisplayName("멤버가 컨텐츠에 댓글을 남길 권한이 없다면, UnAuthorized 예외를 던진다.")
        void ContentCommentServiceImplTest2() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findById(contentId)).willReturn(Optional.of(content));
            given(contentGroupRepository.findAllByContent(content)).willReturn(List.of());
            given(groupMemberRepository.findAllByGroupIn(anyList())).willReturn(List.of(groupMember));
            given(groupMember.getMember()).willReturn(member2);

            // when & then
            assertThatThrownBy(() -> contentCommentService.createComment(memberId, contentId, message))
                    .isInstanceOf(MemberUnAuthorizedException.class);
        }

        @Test
        @DisplayName("댓글을 성공적으로 등록한다.")
        void ContentCommentServiceImplTest3() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findById(contentId)).willReturn(Optional.of(content));
            given(contentGroupRepository.findAllByContent(content)).willReturn(List.of());
            given(groupMemberRepository.findAllByGroupIn(anyList())).willReturn(List.of(groupMember));
            given(groupMember.getMember()).willReturn(member);
            given(idConstructor.create()).willReturn(savedCommentId);
            given(commentRepository.save(any(Comment.class))).willReturn(comment);
            given(comment.getId()).willReturn(savedCommentId);

            // when
            final String commentId = contentCommentService.createComment(memberId, contentId, message);

            // then
            assertThat(commentId).isEqualTo(savedCommentId);
        }
    }
}