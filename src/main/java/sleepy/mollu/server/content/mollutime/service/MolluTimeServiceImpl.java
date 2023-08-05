package sleepy.mollu.server.content.mollutime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.alarm.exception.MolluAlarmNotFoundException;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MolluTimeServiceImpl implements MolluTimeService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final MolluAlarmRepository molluAlarmRepository;
    private final Clock clock;

    @Override
    public SearchMolluTimeResponse searchMolluTime(String memberId) {

        final Optional<Content> optionalContent = getLatestContent(memberId);

        if (optionalContent.isEmpty()) {
            return new SearchMolluTimeResponse(false, null);
        }

        final Content content = optionalContent.get();
        final LocalDateTime todayMolluTime = getTodayMolluTime();
        final LocalDateTime yesterdayMolluTime = getYesterdayMolluTime();

        if (isUploadable(content, todayMolluTime, yesterdayMolluTime)) {
            return new SearchMolluTimeResponse(true, yesterdayMolluTime);
        }

        return new SearchMolluTimeResponse(false, null);
    }

    private Optional<Content> getLatestContent(String memberId) {
        final Member member = getMember(memberId);
        return contentRepository.findTopByMemberOrderByCreatedAtDesc(member);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 회원입니다."));
    }

    private LocalDateTime getTodayMolluTime() {
        return molluAlarmRepository.findTop()
                .orElseThrow(() -> new MolluAlarmNotFoundException("오늘의 MOLLU 타임이 존재하지 않습니다."))
                .getMolluTime();
    }

    private LocalDateTime getYesterdayMolluTime() {
        return molluAlarmRepository.findSecondTop()
                .orElseThrow(() -> new MolluAlarmNotFoundException("어제의 MOLLU 타임이 존재하지 않습니다."))
                .getMolluTime();
    }

    private boolean isUploadable(Content content, LocalDateTime todayMolluTime, LocalDateTime yesterdayMolluTime) {
        final LocalDateTime now = LocalDateTime.now(clock);

        if (now.isBefore(todayMolluTime)) {
            return content.isUploadedBefore(yesterdayMolluTime);
        }

        return content.isUploadedBefore(todayMolluTime);
    }
}
