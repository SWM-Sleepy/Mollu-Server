package sleepy.mollu.server;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sleepy.mollu.server.common.config.JpaAuditingConfig;
import sleepy.mollu.server.common.config.QueryDslConfig;
import sleepy.mollu.server.common.domain.BaseEntity;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.fixture.*;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
public class RepositoryTest {

    protected static final LocalDateTime NOW = LocalDateTime.now();
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
        final Content content = ContentFixture.create(contentId, tag, uploadDateTime, member);
        return contentRepository.save(content);
    }

    protected ContentGroup saveContentGroup(Content content, Group group, LocalDateTime createdAt) throws NoSuchFieldException, IllegalAccessException {
        final ContentGroup contentGroup = ContentGroupFixture.create(content, group);
        reflect(contentGroup, createdAt);
        return contentGroupRepository.save(contentGroup);
    }

    protected List<ContentGroup> saveContentGroups(List<Content> contents, List<Group> groups) {
        return contentGroupRepository.saveAll(ContentGroupFixture.createAll(contents, groups));
    }

    protected void reflect(Object object, Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = BaseEntity.class.getDeclaredField("createdAt");
        field.setAccessible(true);
        field.set(object, value);
    }
}
