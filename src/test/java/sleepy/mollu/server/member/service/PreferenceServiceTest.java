package sleepy.mollu.server.member.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Preference;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.exception.PreferenceNotFoundException;
import sleepy.mollu.server.member.preference.service.PreferenceService;
import sleepy.mollu.server.member.preference.service.PreferenceServiceImpl;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class PreferenceServiceTest {

    private PreferenceService preferenceService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        preferenceService = new PreferenceServiceImpl(memberRepository);
    }

    @Nested
    @DisplayName("[알림 설정 변경 서비스 호출시] ")
    class UpdatePreferenceTest {

        final PreferenceRequest request = new PreferenceRequest(true, true);
        final String memberId = "memberId";

        @Test
        @DisplayName("멤버가 없으면 NotFound 예외를 던진다")
        void UpdatePreferenceTest() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> preferenceService.updatePreference(memberId, request))
                    .isInstanceOf(MemberNotFoundException.class);
        }

        @Test
        @DisplayName("알림 설정이 없으면 NotFound 예외를 던진다")
        void UpdatePreferenceTest2() {
            // given
            final Member member = mock(Member.class);
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(member.getPreference()).willReturn(null);

            // when & then
            assertThatThrownBy(() -> preferenceService.updatePreference(memberId, request))
                    .isInstanceOf(PreferenceNotFoundException.class);
        }

        @Test
        @DisplayName("알림 설정을 성공적으로 변경한다")
        void UpdatePreferenceTest3() {
            // given
            final Member member = mock(Member.class);
            final Preference preference = mock(Preference.class);
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(member.getPreference()).willReturn(preference);

            // when
            preferenceService.updatePreference(memberId, request);

            // then
            then(preference).should().update(request.molluAlarm(), request.contentAlarm());
        }
    }
}