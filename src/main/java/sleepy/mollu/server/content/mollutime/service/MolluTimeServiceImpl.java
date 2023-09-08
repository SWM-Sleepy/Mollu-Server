package sleepy.mollu.server.content.mollutime.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.alarm.domain.MolluAlarm;
import sleepy.mollu.server.alarm.exception.MolluAlarmNotFoundException;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.mollutime.controller.dto.SearchMolluTimeResponse;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MolluTimeServiceImpl implements MolluTimeService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final MolluAlarmRepository molluAlarmRepository;

    @Override
    public SearchMolluTimeResponse searchMolluTime(String memberId) {

        // TODO: 회원가입 직후 컨텐츠를 업로드 할 때, 질문을 보여주는 것으로 기능을 수정한다.
        final Optional<Content> optionalContent = getLatestContent(memberId);

        if (optionalContent.isEmpty()) {
            return emptyResponse();
        }

        final Content content = optionalContent.get();
        final MolluAlarm currentMolluAlarm = getCurrentMolluAlarm();

        if (shouldUploadContent(content, currentMolluAlarm)) {
            return responseFromAlarm(currentMolluAlarm);
        }

        return emptyResponse();
    }

    private Optional<Content> getLatestContent(String memberId) {
        final Member member = getMember(memberId);
        return contentRepository.findTopByMemberOrderByCreatedAtDesc(member);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 회원입니다."));
    }

    private boolean shouldUploadContent(Content content, MolluAlarm molluAlarm) {
        final LocalDateTime molluTime = molluAlarm.getMolluTime();
        return content.isUploadedBefore(molluTime);
    }

    private MolluAlarm getCurrentMolluAlarm() {
        return molluAlarmRepository.findSecondTop()
                .orElseThrow(() -> new MolluAlarmNotFoundException("현재의 MOLLU 타임이 존재하지 않습니다."));
    }

    private SearchMolluTimeResponse emptyResponse() {
        return new SearchMolluTimeResponse(null, null);
    }

    private SearchMolluTimeResponse responseFromAlarm(MolluAlarm molluAlarm) {
        return new SearchMolluTimeResponse(molluAlarm.getMolluTime(), molluAlarm.getQuestion());
    }
}
