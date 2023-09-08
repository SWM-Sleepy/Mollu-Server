package sleepy.mollu.server.content.mollutime.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.exception.MolluAlarmNotFoundException;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MolluTimeServiceImplTest {

    @InjectMocks
    private MolluTimeServiceImpl molluTimeService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private MolluAlarmRepository molluAlarmRepository;

    @Nested
    @DisplayName("[MOLLU 타임 조회 메서드 호출시] ")
    class searchMolluTime {

        final String memberId = "memberId";
        final Member member = mock(Member.class);
        final Content content = mock(Content.class);
        final LocalDateTime now = LocalDateTime.now();
        final MolluAlarm currentMolluAlarm = mock(MolluAlarm.class);

        @Test
        @DisplayName("업로드한 컨텐츠가 없는 경우 MOLLU 타임을 조회할 수 없다.")
        void searchMolluTime0() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.empty());

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.molluTime()).isNull();
        }

        @Test
        @DisplayName("현재의 몰루 타임이 없으면, NotFound 예외를 던진다.")
        void searchMolluTime1() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            given(molluAlarmRepository.findSecondTop()).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> molluTimeService.searchMolluTime(memberId))
                    .isInstanceOf(MolluAlarmNotFoundException.class);
        }

        @Test
        @DisplayName("가장 최근에 업로드한 컨텐츠가 현재의 MOLLU 타임 이전이면, 현재의 MOLLU 타임을 조회할 수 있다.")
        void searchMolluTime2() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            given(molluAlarmRepository.findSecondTop()).willReturn(Optional.of(currentMolluAlarm));
            given(currentMolluAlarm.getMolluTime()).willReturn(now.minusSeconds(1));
            given(content.isUploadedBefore(currentMolluAlarm.getMolluTime())).willReturn(true);

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.molluTime()).isEqualTo(currentMolluAlarm.getMolluTime());
        }

        @Test
        @DisplayName("가장 최근에 업로드한 컨텐츠가 현재의 MOLLU 타임 이후면, 현재의 MOLLU 타임을 조회할 수 없다.")
        void searchMolluTime3() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            given(molluAlarmRepository.findSecondTop()).willReturn(Optional.of(currentMolluAlarm));
            given(currentMolluAlarm.getMolluTime()).willReturn(now.plusSeconds(1));
            given(content.isUploadedBefore(currentMolluAlarm.getMolluTime())).willReturn(false);

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.molluTime()).isNull();
        }
    }
}