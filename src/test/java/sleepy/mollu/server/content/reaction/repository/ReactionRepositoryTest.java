package sleepy.mollu.server.content.reaction.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sleepy.mollu.server.RepositoryTest;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.reaction.domain.Reaction;
import sleepy.mollu.server.member.domain.Member;

import java.util.List;

class ReactionRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("[findAllByContentExcludesMember 호출시] 컨텐츠에 대한 멤버의 반응을 제외한 모든 반응을 조회한다.")
    void ReactionRepositoryTest0() {

        // given
        final Member member1 = saveMember("member1", "mollu_1");
        final Member member2 = saveMember("member2", "mollu_2");
        final Content content = saveContent("content1", "tag", NOW, member1);
        final Reaction reaction1 = saveReaction("reaction1", member1, content);
        final Reaction reaction2 = saveReaction("reaction2", member2, content);

        // when
        final List<Reaction> reactions = reactionRepository.findAllByContentExcludesMember(content, member1);

        // then
        Assertions.assertThat(reactions).containsExactly(reaction2);
    }
}