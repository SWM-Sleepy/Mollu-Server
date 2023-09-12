package sleepy.mollu.server.content.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ContentRepositoryTest extends RepositoryTest {

    public static Stream<Arguments> findAllByMemberAndDateSource() {
        return Stream.of(
                Arguments.of(NOW_DATE.plusDays(2), NOW_DATE.plusDays(3), 2),
                Arguments.of(NOW_DATE.plusDays(1), NOW_DATE.plusDays(3), 3),
                Arguments.of(NOW_DATE.plusDays(2), NOW_DATE.plusDays(4), 3),
                Arguments.of(NOW_DATE.minusDays(1), NOW_DATE, 0),
                Arguments.of(NOW_DATE.plusDays(5), NOW_DATE.plusDays(6), 0)
        );
    }

    @Test
    @DisplayName("[findTopByMemberOrderByCreatedAtDesc 호출시] 멤버가 찍은 가장 최근의 컨텐츠를 조회한다.")
    void ContentRepositoryTest() {
        // given
        final Member member = saveMember("memberId", "mollu");
        final Group group = saveGroup("groupId");
        final Content content1 = saveContent("contentId1", "tag1", NOW, member);
        final Content content2 = saveContent("contentId2", "tag2", NOW, member);
        final Content content3 = saveContent("contentId3", "tag3", NOW, member);

        saveContentGroups(List.of(content1, content2, content3), List.of(group));

        // when
        final Content content = contentRepository.findTopByMemberOrderByCreatedAtDesc(member)
                .orElseThrow();

        // then
        assertThat(content).isSameAs(content3);
    }

    @ParameterizedTest
    @DisplayName("[findAllByMemberAndDate 호출시] 멤버가 업로드한 컨텐츠를 조회한다.")
    @MethodSource("findAllByMemberAndDateSource")
    void ContentRepositoryTest2(LocalDate from, LocalDate to, int expectedSize) {
        // given
        final Member member = saveMember("memberId", "mollu");
        saveContent("contentId1", "tag1", NOW.plusDays(1), member);
        saveContent("contentId2", "tag2", NOW.plusDays(2), member);
        saveContent("contentId3", "tag3", NOW.plusDays(3), member);
        saveContent("contentId4", "tag4", NOW.plusDays(4), member);

        // when
        final List<Content> contents = contentRepository.findAllByMemberAndDate(member, from.atTime(0, 0), to.atTime(23, 59));

        // then
        assertAll(
                () -> assertThat(contents).extracting(Content::getUploadDateTime).isSortedAccordingTo(Comparator.reverseOrder()),
                () -> assertThat(contents).hasSize(expectedSize)
        );
    }

    @Test
    @DisplayName("[findAllByMember 호출시] 멤버가 업로드한 컨텐츠를 최신순으로 조회한다.")
    void ContentRepositoryTest3() {
        // given
        final Member member = saveMember("memberId", "mollu");
        saveContent("contentId1", "tag1", NOW.plusDays(1), member);
        saveContent("contentId2", "tag1", NOW.plusDays(2), member);
        saveContent("contentId3", "tag1", NOW.plusDays(3), member);
        saveContent("contentId4", "tag1", NOW.plusDays(4), member);

        // when
        final List<Content> contents = contentRepository.findAllByMember(member);

        // then
        assertThat(contents).extracting(Content::getId)
                .containsExactly("contentId4", "contentId3", "contentId2", "contentId1");
    }
}