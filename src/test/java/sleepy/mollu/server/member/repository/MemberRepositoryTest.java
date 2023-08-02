package sleepy.mollu.server.member.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import sleepy.mollu.server.common.config.JpaAuditingConfig;
import sleepy.mollu.server.common.config.QueryDslConfig;
import sleepy.mollu.server.fixture.MemberFixture;
import sleepy.mollu.server.member.domain.Member;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QueryDslConfig.class)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("[existsByMolluId 호출시] 동일한 molluId를 가진 멤버가 존재하면 true를 반환한다.")
    void MemberRepositoryTest() {
        // given
        final String sameMolluId = "molluId";
        saveMember(sameMolluId);

        // when
        final boolean exists = memberRepository.existsByMolluId(sameMolluId);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("[existsByMolluId 호출시] 동일한 molluId를 가진 멤버가 없으면 false를 반환한다.")
    void MemberRepositoryTest2() {
        // given
        final String saveMolluId = "saveMolluId";
        final String checkMolluId = "checkMolluId";
        saveMember(saveMolluId);

        // when
        final boolean exists = memberRepository.existsByMolluId(checkMolluId);

        // then
        assertThat(exists).isFalse();
    }

    private void saveMember(String molluId) {
        final Member member = MemberFixture.create("memberId", molluId);
        memberRepository.save(member);
    }
}