package sleepy.mollu.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.controller.dto.MyCalendarResponse;
import sleepy.mollu.server.member.controller.dto.MyContentsResponse;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.content.SearchRange;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final GroupRepository groupRepository;

    private static MyCalendarResponse.CalendarResponse getCalendarResponse(Content content) {
        return new MyCalendarResponse.CalendarResponse(
                content.getId(),
                content.getUploadDateTime(),
                content.getThumbnailFrontSource());
    }

    @Override
    public MyContentsResponse searchMyContents(String memberId, LocalDate date) {
        final Member member = getMember(memberId);
        final SearchRange range = SearchRange.from(date);
        final List<Content> contents = contentRepository.findAllByMemberAndDate(member, range.getFrom(), range.getTo());

        return getMyContentsResponse(contents);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "] 에 해당하는 멤버가 없습니다."));
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

    @Override
    public MyCalendarResponse searchCalendar(String memberId) {
        final Member member = getMember(memberId);
        final List<Content> contents = getContents(member);

        return getMyCalendarResponse(contents);
    }

    private List<Content> getContents(Member member) {
        return contentRepository.findAllByMember(member);
    }

    private MyCalendarResponse getMyCalendarResponse(List<Content> contents) {
        return new MyCalendarResponse(contents.stream()
                .map(MemberServiceImpl::getCalendarResponse)
                .toList());
    }

    @Transactional
    @Override
    public void deleteMember(String memberId) {
        final Member member = getMember(memberId);

        memberRepository.delete(member);
    }
}
