package sleepy.mollu.server.content.contentgroup.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentGroupRepositoryTest extends RepositoryTest {

    private Member member1, member2;
    private Group group1;
    private Content content1, content2, content3;

    @BeforeEach
    void setUp() {
        member1 = saveMember("member1", "molluId1");
        member2 = saveMember("member2", "molluId2");
        group1 = saveGroup("group1");

        final List<Member> members = List.of(member1, member2);
        final List<Group> groups = List.of(group1);
        saveGroupMembers(groups, members);

        content1 = saveContent("content1", "tag1", NOW, member1);
        content2 = saveContent("content2", "tag2", NOW, member2);
        content3 = saveContent("content3", "tag3", NOW, member2);

        final List<Content> contents = List.of(content1, content2, content3);
        saveContentGroups(contents, groups);

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

            final Content content4 = saveContent("content4", "tag4", NOW, member2);
            saveContentGroup(content4, group1);

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