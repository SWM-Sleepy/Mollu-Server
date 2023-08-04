package sleepy.mollu.server.content.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("[findTopByMemberOrderByCreatedAtDesc 호출시] 멤버가 찍은 가장 최근의 컨텐츠를 조회한다.")
    void ContentRepositoryTest() {
        // given
        final Member member = saveMember("memberId", "molluId");
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
}