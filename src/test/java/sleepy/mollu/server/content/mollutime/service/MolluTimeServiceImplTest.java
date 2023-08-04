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

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Mock
    private Clock clock;

    @Nested
    @DisplayName("[MOLLU 타임 조회 메서드 호출시] ")
    class searchMolluTime {

        final String memberId = "memberId";
        final Member member = mock(Member.class);
        final Content content = mock(Content.class);
        final LocalDateTime now = LocalDateTime.now();
        final MolluAlarm todayMolluAlarm = mock(MolluAlarm.class);
        final MolluAlarm yesterdayMolluAlarm = mock(MolluAlarm.class);

        @Test
        @DisplayName("업로드한 컨텐츠가 없는 경우 MOLLU 타임을 조회할 수 없다.")
        void searchMolluTime0() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.empty());

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.available()).isFalse();
        }

        @Test
        @DisplayName("오늘의 MOLLU 타임이 없으면, NotFound 예외를 던진다.")
        void searchMolluTime1() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            given(molluAlarmRepository.findTop()).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> molluTimeService.searchMolluTime(memberId))
                    .isInstanceOf(MolluAlarmNotFoundException.class);
        }

        @Test
        @DisplayName("어제의 MOLLU 타임이 없으면, NotFound 예외를 던진다.")
        void searchMolluTime2() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            setTodayMolluTime(now);
            given(molluAlarmRepository.findSecondTop()).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> molluTimeService.searchMolluTime(memberId))
                    .isInstanceOf(MolluAlarmNotFoundException.class);
        }

        @Test
        @DisplayName("현재 시각이 오늘의 MOLLU 타임 이전이고, 가장 최근에 업로드한 컨텐츠가 어제의 MOLLU 타임 이전이면, 어제의 MOLLU 타임을 조회할 수 있다.")
        void searchMolluTime3() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            setTodayMolluTime(now.plusSeconds(1));
            setYesterdayMolluTime(now);
            setClock(now);
            given(content.isUploadedBefore(yesterdayMolluAlarm.getMolluTime())).willReturn(true);

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.available()).isTrue();
        }

        @Test
        @DisplayName("현재 시각이 오늘의 MOLLU 타임 이전이고, 가장 최근에 업로드한 컨텐츠가 어제의 MOLLU 타임 이후이면, MOLLU 타임을 조회할 수 없다.")
        void searchMolluTime4() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            setTodayMolluTime(now.plusSeconds(1));
            setYesterdayMolluTime(now);
            setClock(now);
            given(content.isUploadedBefore(yesterdayMolluAlarm.getMolluTime())).willReturn(false);

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.available()).isFalse();
        }

        @Test
        @DisplayName("현재 시각이 오늘의 MOLLU 타임 이후이고, 가장 최근에 업로드한 컨텐츠가 오늘의 MOLLU 타임 이전이면, MOLLU 타임을 조회할 수 있다.")
        void searchMolluTime5() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            setTodayMolluTime(now.minusSeconds(1));
            setYesterdayMolluTime(now);
            setClock(now);
            given(content.isUploadedBefore(todayMolluAlarm.getMolluTime())).willReturn(true);

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.available()).isTrue();
        }

        @Test
        @DisplayName("현재 시각이 오늘의 MOLLU 타임 이후이고, 가장 최근에 업로드한 컨텐츠가 오늘의 MOLLU 타임 이후이면, MOLLU 타임을 조회할 수 없다.")
        void searchMolluTime6() {
            // given
            given(memberRepository.findById(memberId)).willReturn(Optional.of(member));
            given(contentRepository.findTopByMemberOrderByCreatedAtDesc(member)).willReturn(Optional.of(content));
            setTodayMolluTime(now.minusSeconds(1));
            setYesterdayMolluTime(now);
            setClock(now);
            given(content.isUploadedBefore(todayMolluAlarm.getMolluTime())).willReturn(false);

            // when
            final SearchMolluTimeResponse response = molluTimeService.searchMolluTime(memberId);

            // then
            assertThat(response.available()).isFalse();
        }

        private void setClock(LocalDateTime localDateTime) {
            given(clock.instant()).willReturn(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            given(clock.getZone()).willReturn(ZoneId.systemDefault());
        }

        private void setTodayMolluTime(LocalDateTime localDateTime) {
            given(molluAlarmRepository.findTop()).willReturn(Optional.of(todayMolluAlarm));
            given(todayMolluAlarm.getMolluTime()).willReturn(localDateTime);
        }

        private void setYesterdayMolluTime(LocalDateTime localDateTime) {
            given(molluAlarmRepository.findSecondTop()).willReturn(Optional.of(yesterdayMolluAlarm));
            given(yesterdayMolluAlarm.getMolluTime()).willReturn(localDateTime);
        }
    }
}