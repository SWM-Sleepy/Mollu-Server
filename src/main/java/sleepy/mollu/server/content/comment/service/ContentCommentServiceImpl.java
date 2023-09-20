package sleepy.mollu.server.content.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentResponse;
import sleepy.mollu.server.content.comment.controller.dto.SearchCommentResponse.CommentResponse;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.comment.exception.CommentNotFoundException;
import sleepy.mollu.server.content.comment.repository.CommentRepository;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.content.report.repository.CommentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberCommentUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.member.service.AuthorizationService;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentCommentServiceImpl implements ContentCommentService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentGroupRepository contentGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;

    private final IdConstructor idConstructor;
    private final AuthorizationService authorizationService;

    @Transactional
    @Override
    public String createComment(String memberId, String contentId, String comment) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        authorizationService.authorizeMemberForContent(member, content);

        return saveComment(comment, member, content);

        // TODO: 알림 전송 로직 작성
    }

    private String saveComment(String comment, Member member, Content content) {
        final Comment newComment = commentRepository.save(Comment.builder()
                .id(idConstructor.create())
                .message(comment)
                .member(member)
                .content(content)
                .build());
        return newComment.getId();
    }

    @Override
    public SearchCommentResponse searchComment(String memberId, String contentId) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        authorizationService.authorizeMemberForContent(member, content);
        final List<Comment> filteredComments = getFilteredComments(member, content);

        return getSearchCommentResponse(filteredComments);
    }

    private List<Comment> getFilteredComments(Member member, Content content) {
        final List<Comment> reportedComments = getReportedComments(member);
        final List<Comment> comments = getCommentsBy(content);
        return comments.stream()
                .filter(comment -> !reportedComments.contains(comment))
                .toList();
    }

    private List<Comment> getReportedComments(Member member) {
        final List<CommentReport> commentReports = commentReportRepository.findAllByMember(member);
        return commentReports.stream()
                .map(CommentReport::getComment)
                .toList();
    }

    private List<Comment> getCommentsBy(Content content) {
        return commentRepository.findAllWithMemberByContent(content);
    }

    private SearchCommentResponse getSearchCommentResponse(List<Comment> comments) {
        return new SearchCommentResponse(comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getMessage(),
                        comment.getMemberId(),
                        comment.getMemberName(),
                        comment.getMemberProfileSource(),
                        comment.getCreatedAt()))
                .toList());
    }

    @Transactional
    @Override
    public void deleteComment(String memberId, String contentId, String commentId) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        final Comment comment = getComment(commentId);

        authorizationService.authorizeMemberForContent(member, content);
        authorizeMemberForComment(member, comment);

        commentRepository.delete(comment);
    }

    private Comment getComment(String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("ID가 [" + commentId + "]인 댓글을 찾을 수 없습니다."));
    }

    private void authorizeMemberForComment(Member member, Comment comment) {
        if (!comment.isOwner(member)) {
            throw new MemberCommentUnAuthorizedException("해당 댓글에 대한 삭제 권한이 없습니다.");
        }
    }
}