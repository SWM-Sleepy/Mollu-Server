package sleepy.mollu.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.controller.dto.MyContentsResponse;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final int FROM_HOUR = 0;
    private static final int FROM_MINUTE = 0;
    private static final int TO_HOUR = 23;
    private static final int TO_MINUTE = 59;
    private static final int SEARCH_PERIOD = 7;

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final GroupRepository groupRepository;

    @Override
    public MyContentsResponse searchMyContents(String memberId, LocalDate date) {
        final Member member = getMember("memberId");
        final LocalDateTime from = getFromDateTime(date.minusDays(SEARCH_PERIOD));
        final LocalDateTime to = getToDateTime(date);
        final List<Content> contents = contentRepository.findAllByMemberAndDate(member, from, to);

        return getMyContentsResponse(contents);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "] 에 해당하는 멤버가 없습니다."));
    }

    private LocalDateTime getFromDateTime(LocalDate date) {
        return date.atTime(FROM_HOUR, FROM_MINUTE);
    }

    private LocalDateTime getToDateTime(LocalDate date) {
        return date.atTime(TO_HOUR, TO_MINUTE);
    }

    private Group getGroup() {
        return groupRepository.findDefaultGroup()
                .orElseThrow(() -> new GroupNotFoundException("디폴트 그룹이 존재하지 않습니다."));
    }


    private MyContentsResponse getMyContentsResponse(List<Content> contents) {
        final Group group = getGroup();
        return new MyContentsResponse(contents.stream()
                .map(content -> getMyContent(content, group.getName()))
                .toList());
    }

    private MyContentsResponse.MyContent getMyContent(Content content, String groupName) {
        return new MyContentsResponse.MyContent(
                content.getId(),
                content.getLocation(),
                groupName,
                content.getMolluDateTime(),
                content.getUploadDateTime(),
                content.getContentTag(),
                content.getFrontContentSource(),
                content.getBackContentSource());
    }

    @Transactional
    @Override
    public void deleteMember(String memberId) {
        final Member member = getMember(memberId);

        memberRepository.delete(member);
    }
}
