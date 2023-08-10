package sleepy.mollu.server.content.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.content.report.exception.ReportBadRequestException;
import sleepy.mollu.server.content.report.repository.ContentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final ContentReportRepository contentReportRepository;

    @Override
    public Long reportContent(String memberId, String contentId, String reason) {

        final Member member = getMember(memberId);
        final Content content = getContent(contentId);
        validateOwner(member, content);

        return saveContentReport(reason, member, content);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("ID가 [" + memberId + "]인 멤버를 찾을 수 없습니다."));
    }

    private Content getContent(String contentId) {
        return contentRepository.findById(contentId)
                .orElseThrow(() -> new MemberNotFoundException("ID가 [" + contentId + "]인 컨텐츠를 찾을 수 없습니다."));
    }

    private void validateOwner(Member member, Content content) {
        if (content.isOwner(member)) {
            throw new ReportBadRequestException("자신의 컨텐츠는 신고할 수 없습니다.");
        }
    }

    private Long saveContentReport(String reason, Member member, Content content) {
        return contentReportRepository.save(ContentReport.builder()
                        .reason(reason)
                        .member(member)
                        .content(content)
                        .build())
                .getId();
    }
}
