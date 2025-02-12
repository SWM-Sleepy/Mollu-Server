package sleepy.mollu.server.content.comment.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentPreviewResponse;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.comment.repository.CommentRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.repository.CommentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberContentUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.member.service.AuthorizationService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
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
    private CommentRepository commentRepository;

    @Mock
    private CommentReportRepository commentReportRepository;

    @Mock
    private IdConstructor idConstructor;

    @Mock
    private AuthorizationService authorizationService;

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
        @DisplayName("멤버가 컨텐츠에 댓글을 남길 권한이 없다면, UnAuthorized 예외를 던진다.")
        void ContentCommentServiceImplTest2() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            willThrow(MemberContentUnAuthorizedException.class).given(authorizationService).authorizeMemberForContent(member, content);
            given(groupMember.getMember()).willReturn(member2);

            // when & then
            assertThatThrownBy(() -> contentCommentService.createComment(memberId, contentId, message))
                    .isInstanceOf(MemberContentUnAuthorizedException.class);
        }

        @Test
        @DisplayName("댓글을 성공적으로 등록한다.")
        void ContentCommentServiceImplTest3() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
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

    @Nested
    @DisplayName("[댓글 미리 보기 서비스 호출시] ")
    class SearchCommentPreview {

        final String memberId = "memberId";
        final String contentId = "contentId";

        final Member member = mock(Member.class);
        final Content content = mock(Content.class);
        final Comment comment = mock(Comment.class);

        @Test
        @DisplayName("댓글이 없다면 comment 변수가 null인 SearchCommentPreviewResponse를 반환한다.")
        void SearchCommentPreview() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(commentReportRepository.findAllByMember(member)).willReturn(List.of());
            given(commentRepository.countByContent(any(Content.class), anyList())).willReturn(0L);
            given(commentRepository.findTop(any(Content.class), anyList())).willReturn(Optional.empty());

            // when
            final SearchCommentPreviewResponse response = contentCommentService.searchCommentPreview(memberId, contentId);

            // then
            assertAll(
                    () -> assertThat(response.commentNum()).isZero(),
                    () -> assertThat(response.comment()).isNull()
            );
        }

        @Test
        @DisplayName("댓글이 있다면 comment 변수가 null이 아닌 SearchCommentPreviewResponse를 반환한다.")
        void SearchCommentPreview1() {
            // given
            given(memberRepository.findByIdOrElseThrow(memberId)).willReturn(member);
            given(contentRepository.findByIdOrElseThrow(contentId)).willReturn(content);
            given(commentReportRepository.findAllByMember(member)).willReturn(List.of());
            given(commentRepository.countByContent(any(Content.class), anyList())).willReturn(2L);
            given(commentRepository.findTop(any(Content.class), anyList())).willReturn(Optional.of(comment));

            // when
            final SearchCommentPreviewResponse response = contentCommentService.searchCommentPreview(memberId, contentId);

            // then
            assertAll(
                    () -> assertThat(response.commentNum()).isEqualTo(2L),
                    () -> assertThat(response.comment()).isNotNull()
            );
        }
    }
}