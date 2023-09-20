package sleepy.mollu.server.content.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.comment.exception.CommentNotFoundException;
import sleepy.mollu.server.content.comment.repository.CommentRepository;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.controller.dto.CommentReportResponse;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.content.report.exception.ReportBadRequestException;
import sleepy.mollu.server.content.report.repository.CommentReportRepository;
import sleepy.mollu.server.content.report.repository.ContentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentReportRepository contentReportRepository;
    private final CommentRepository commentRepository;
    private final ContentGroupRepository contentGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CommentReportRepository commentReportRepository;

    @Override
    public Long reportContent(String memberId, String contentId, String reason) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = getContent(contentId);
        validateOwner(member, content);
        authorizeMemberForContent(member, content);

        return saveContentReport(reason, member, content);
    }

    private Content getContent(String contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new MemberNotFoundException("ID가 [" + contentId + "]인 컨텐츠를 찾을 수 없습니다."));
    }

    private void validateOwner(Member member, Content content) {
        if (content.isOwner(member)) {
            throw new ReportBadRequestException("자신의 컨텐츠는 신고할 수 없습니다.");
        }
    }

    private Long saveContentReport(String reason, Member member, Content content) {
        return contentReportRepository.save(ContentReport.builder()
                        .reason(reason)
                        .member(member)
                        .content(content)
                        .build())
                .getId();
    }

    @Override
    public CommentReportResponse reportComment(String memberId, String contentId, String commentId, String reason) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = getContent(contentId);
        final Comment comment = getComment(commentId);

        validateOwner(member, comment);
        authorizeMemberForContent(member, content);
        authorizeMemberForComment(content, comment);

        final CommentReport commentReport = saveCommentReport(reason, member, comment);
        return getCommentReportResponse(commentReport);
    }

    private Comment getComment(String commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("ID가 [" + commentId + "]인 댓글을 찾을 수 없습니다."));
    }

    private void validateOwner(Member member, Comment comment) {
        if (comment.isOwner(member)) {
            throw new ReportBadRequestException("자신의 댓글은 신고할 수 없습니다.");
        }
    }

    private void authorizeMemberForContent(Member member, Content content) {
        final List<Member> membersByGroups = getGroupMembersByContent(content);
        if (!membersByGroups.contains(member)) {
            throw new MemberUnAuthorizedException("해당 컨텐츠에 대한 접근 권한이 없습니다.");
        }
    }

    private List<Member> getGroupMembersByContent(Content content) {
        final List<Group> groupsByContent = contentGroupRepository.findAllByContent(content)
                .stream()
                .map(ContentGroup::getGroup)
                .toList();
        return groupMemberRepository.findAllByGroupIn(groupsByContent)
                .stream()
                .map(GroupMember::getMember)
                .toList();
    }

    private void authorizeMemberForComment(Content content, Comment comment) {
        if (!comment.belongsTo(content)) {
            throw new MemberUnAuthorizedException("해당 댓글에 대한 접근 권한이 없습니다.");
        }
    }

    private CommentReport saveCommentReport(String reason, Member member, Comment comment) {
        return commentReportRepository.save(CommentReport.builder()
                .reason(reason)
                .member(member)
                .comment(comment)
                .build());
    }

    private CommentReportResponse getCommentReportResponse(CommentReport commentReport) {
        return new CommentReportResponse(
                commentReport.getId(),
                commentReport.getReason(),
                commentReport.getMember().getId(),
                commentReport.getComment().getId());
    }
}
