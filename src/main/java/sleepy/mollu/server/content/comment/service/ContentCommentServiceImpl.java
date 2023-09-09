package sleepy.mollu.server.content.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.comment.repository.CommentRepository;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.exception.ContentNotFoundException;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentCommentServiceImpl implements ContentCommentService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentGroupRepository contentGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final CommentRepository commentRepository;
    private final IdConstructor idConstructor;

    @Override
    public String createComment(String memberId, String contentId, String comment) {
        final Member member = getMember(memberId);
        final Content content = getContent(contentId);
        authorizeMemberForContent(member, content);

        return saveComment(comment, member, content);

        // TODO: 알림 전송 로직 작성
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "] 는 존재하지 않는 회원입니다."));
    }

    private Content getContent(String contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("ID가 [" + contentId + "]인 컨텐츠를 찾을 수 없습니다."));
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

    private void authorizeMemberForContent(Member member, Content content) {
        final List<Member> membersByGroups = getGroupMembersByContent(content);
        if (!membersByGroups.contains(member)) {
            throw new MemberUnAuthorizedException("해당 컨텐츠에 대한 접근 권한이 없습니다.");
        }
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


}
