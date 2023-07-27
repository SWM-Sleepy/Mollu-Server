package sleepy.mollu.server.member.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Preference;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = MemberRepository.class))
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
        final Preference preference = Preference.builder()
                .molluAlarm(true)
                .contentAlarm(true)
                .build();

        memberRepository.save(Member.builder()
                .id("memberId")
                .molluId(molluId)
                .name("name")
                .birthday(LocalDate.now())
                .preference(preference)
                .build());
    }
}