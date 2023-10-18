package sleepy.mollu.server;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sleepy.mollu.server.common.config.JpaAuditingConfig;
import sleepy.mollu.server.common.config.QueryDslConfig;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.comment.repository.CommentRepository;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.content.reaction.repository.ReactionRepository;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.content.report.repository.CommentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.fixture.*;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
public class RepositoryTest {

    protected static final LocalDateTime NOW = LocalDateTime.now();
    protected static final LocalDate NOW_DATE = LocalDate.now();
    @Autowired
    protected MemberRepository memberRepository;
    @Autowired
    protected GroupRepository groupRepository;
    @Autowired
    protected GroupMemberRepository groupMemberRepository;
    @Autowired
    protected ContentRepository contentRepository;
    @Autowired
    protected ContentGroupRepository contentGroupRepository;
    @Autowired
    protected ReactionRepository reactionRepository;
    @Autowired
    protected CommentRepository commentRepository;
    @Autowired
    protected CommentReportRepository commentReportRepository;
    @Autowired
    protected EntityManager em;

    protected Member saveMember(String memberId, String molluId) {
        final Member member = MemberFixture.create(memberId, molluId);
        return memberRepository.save(member);
    }

    protected Group saveGroup(String groupId) {
        final Group group = GroupFixture.create(groupId);
        return groupRepository.save(group);
    }

    protected void saveGroupMembers(List<Group> groups, List<Member> members) {
        groupMemberRepository.saveAll(GroupMemberFixture.createAll(groups, members));
    }

    protected Content saveContent(String contentId, String tag, LocalDateTime uploadDateTime, Member member) {
        return contentRepository.save(ContentFixture.create(contentId, tag, uploadDateTime, member));
    }

    protected ContentGroup saveContentGroup(Content content, Group group) {
        return contentGroupRepository.save(ContentGroupFixture.create(content, group));
    }

    protected void saveContentGroups(List<Content> contents, List<Group> groups) {
        contentGroupRepository.saveAll(ContentGroupFixture.createAll(contents, groups));
    }

    protected Reaction saveReaction(String reactionId, Member member, Content content) {
        return reactionRepository.save(ReactionFixture.create(reactionId, member, content));
    }

    protected Comment saveComment(String commentId, Member member, Content content) {
        return commentRepository.save(CommentFixture.create(commentId, member, content));
    }

    protected CommentReport saveCommentReport(Member member, Comment comment) {
        return commentReportRepository.save(CommentReportFixture.create(member, comment));
    }

    protected void reflect(Object object, Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = BaseEntity.class.getDeclaredField("createdAt");
        field.setAccessible(true);
        field.set(object, value);
    }
}
