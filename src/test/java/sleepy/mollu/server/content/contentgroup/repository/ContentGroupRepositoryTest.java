package sleepy.mollu.server.content.contentgroup.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sleepy.mollu.server.common.config.JpaAuditingConfig;
import sleepy.mollu.server.common.config.QueryDslConfig;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.fixture.*;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDslConfig.class})
class ContentGroupRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private ContentGroupRepository contentGroupRepository;

    @Autowired
    private EntityManager em;

    private static final LocalDateTime NOW = LocalDateTime.now();

    private Member member1, member2;
    private Group group1;
    private Content content1, content2, content3;

    @BeforeEach
    void setUp() {
        member1 = MemberFixture.create("member1", "molluId1");
        member2 = MemberFixture.create("member2", "molluId2");
        group1 = GroupFixture.create("group1");

        final List<Member> members = List.of(member1, member2);
        final List<Group> groups = List.of(group1);
        memberRepository.saveAll(members);
        groupRepository.saveAll(groups);
        groupMemberRepository.saveAll(GroupMemberFixture.createAll(groups, members));

        content1 = ContentFixture.create("content1", "tag1", NOW, member1);
        content2 = ContentFixture.create("content2", "tag2", NOW, member2);
        content3 = ContentFixture.create("content3", "tag3", NOW, member2);

        final List<Content> contents = List.of(content1, content2, content3);
        contentRepository.saveAll(contents);
        contentGroupRepository.saveAll(ContentGroupFixture.createAll(contents, groups));

        em.flush();
        em.clear();
    }

    @Nested
    @DisplayName("[findGroupFeed 호출시] ")
    class FindGroupFeed {

        @Test
        @DisplayName("그룹에 속한 컨텐츠 그룹을 컨텐츠, 멤버와 함께 조회한다.")
        void FindGroupFeed0() {
            // given & when
            System.out.println("--------------------");
            final List<ContentGroup> feed = contentGroupRepository.findGroupFeed(List.of(group1), 1, null, null);
            System.out.println("--------------------");
            System.out.println("--------------------");
            final List<String> findMolluIds = feed.stream()
                    .map(ContentGroup::getContent)
                    .map(Content::getMember)
                    .map(Member::getMolluId)
                    .toList();
            System.out.println("--------------------");
            // then
            assertThat(findMolluIds).containsExactly("molluId2");
        }

        @Test
        @DisplayName("첫 번째 페이지 조회에 성공한다.")
        void FindGroupFeed1() {
            // given & when
            final List<ContentGroup> feed = contentGroupRepository.findGroupFeed(List.of(group1), 2, null, null);
            final List<String> findTags = feed.stream()
                    .map(ContentGroup::getContent)
                    .map(Content::getContentTag)
                    .toList();
            // then
            assertThat(findTags).containsExactly("tag3", "tag2");
        }

        @Test
        @DisplayName("피드 조회 도중 다른 컨텐츠가 업로드되어도, 피드에는 영향을 주지 않는다.")
        void FindGroupFeed3() {
            // given
            final List<ContentGroup> expectedFeed = contentGroupRepository.findGroupFeed(List.of(group1), 1, null, null);
            final ContentGroup lastContentGroup = expectedFeed.get(expectedFeed.size() - 1);

            final Content content = ContentFixture.create("content4", "tag4", NOW, member2);
            final ContentGroup contentGroup = ContentGroupFixture.create(content, group1);
            contentRepository.save(content);
            contentGroupRepository.save(contentGroup);

            // when
            final List<ContentGroup> findFeed = contentGroupRepository.findGroupFeed(List.of(group1), 1, lastContentGroup.getId(), lastContentGroup.getCreatedAt());
            final List<String> findTags = findFeed.stream()
                    .map(ContentGroup::getContent)
                    .map(Content::getContentTag)
                    .toList();

            // then
            assertThat(findTags).containsExactly("tag2");
        }

        @Test
        @DisplayName("생성날짜가 중복된 컨텐츠가 있어도, 중복된 피드를 조회하지 않는다.")
        void FindGroupFeed5() {
            // given

            // when

            // then
        }

        @Test
        @DisplayName("피드의 마지막에는 컨텐츠가 없다.")
        void FindGroupFeed4() {
            // given

            // when

            // then
        }
    }
}