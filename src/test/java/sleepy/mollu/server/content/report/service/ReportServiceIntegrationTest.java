package sleepy.mollu.server.content.report.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.content.report.domain.Report;
import sleepy.mollu.server.content.report.dto.ReportRequest;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Preference;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ReportServiceIntegrationTest {

    @Autowired
    private ReportService reportService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ContentRepository contentRepository;

    @Test
    @DisplayName("[컨텐츠 신고 서비스 호출 시] 신고 엔티티가 저장된다.")
    void ReportServiceIntegrationTest() {
        // given
        final Preference preference = Preference.builder()
                .molluAlarm(true)
                .contentAlarm(true)
                .build();

        final Member member = memberRepository.save(Member.builder()
                .id("memberId")
                .name("name")
                .birthday(LocalDate.now())
                .molluId("molluId")
                .preference(preference)
                .build());

        final Member member2 = memberRepository.save(Member.builder()
                .id("memberId2")
                .name("name")
                .birthday(LocalDate.now())
                .molluId("molluId")
                .preference(preference)
                .build());

        final Content content = contentRepository.save(Content.builder()
                .id("contentId")
                .member(member)
                .contentTag("tag")
                .build());

        // when
        reportService.reportContent("memberId2", "contentId", new ReportRequest("reason"));

        final List<Report> memberContentReports = member2.getContentReports();
        final List<ContentReport> contentReports = content.getReports();

        // then
        assertAll(
                () -> assertThat(memberContentReports).hasSize(1),
                () -> assertThat(contentReports).hasSize(1)
        );
    }

}