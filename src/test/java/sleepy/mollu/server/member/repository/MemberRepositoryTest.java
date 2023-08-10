package sleepy.mollu.server.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sleepy.mollu.server.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {

    @Test
    @DisplayName("[existsByMolluId 호출시] 동일한 molluId를 가진 멤버가 존재하면 true를 반환한다.")
    void MemberRepositoryTest() {
        // given
        final String sameMolluId = "mollu";
        saveMember("memberId", sameMolluId);

        // when
        final boolean exists = memberRepository.existsByMolluId(sameMolluId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("[existsByMolluId 호출시] 동일한 molluId를 가진 멤버가 없으면 false를 반환한다.")
    void MemberRepositoryTest2() {
        // given
        final String saveMolluId = "mollua";
        final String checkMolluId = "mollub";
        saveMember("memberId", saveMolluId);

        // when
        final boolean exists = memberRepository.existsByMolluId(checkMolluId);

        // then
        assertThat(exists).isFalse();
    }
}