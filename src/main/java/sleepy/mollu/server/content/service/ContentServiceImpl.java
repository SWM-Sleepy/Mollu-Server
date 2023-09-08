package sleepy.mollu.server.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.content.ContentSource;
import sleepy.mollu.server.content.domain.content.ContentTime;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchContentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.exception.ContentNotFoundException;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.content.report.repository.ContentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private static final int PAGE_SIZE = 15;

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ContentGroupRepository contentGroupRepository;
    private final ContentReportRepository contentReportRepository;

    private final IdConstructor idConstructor;
    private final FileHandler fileHandler;

    // TODO: 로직 수정 및 테스트 코드 작성
    @Override
    public GroupSearchFeedResponse searchGroupFeed(String memberId, String cursorId, LocalDateTime cursorEndDate) {

        final Member member = getMember(memberId);
        final List<Group> groups = getGroups(member);
        final List<ContentGroup> contentGroups = getContentGroups(groups, cursorId, cursorEndDate);
        final List<Content> contents = getContents(contentGroups, member);
        final Cursor cursor = getCursor(contentGroups);

        return getGroupSearchFeedResponse(cursor, contents);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "] 는 존재하지 않는 회원입니다."));
    }

    private List<Group> getGroups(Member member) {
        final List<GroupMember> groupMembers = groupMemberRepository.findAllWithGroupByMember(member);

        return groupMembers.stream()
                .map(GroupMember::getGroup)
                .toList();
    }

    private List<ContentGroup> getContentGroups(List<Group> groups, String cursorId, LocalDateTime cursorEndDate) {
        return contentGroupRepository.findGroupFeed(groups, PAGE_SIZE, cursorId, cursorEndDate);
    }

    private List<Content> getContents(List<ContentGroup> contentGroups, Member member) {
        final List<ContentReport> contentReports = getContentReports(member);
        final List<Content> reportedContents = contentReports.stream()
                .map(ContentReport::getContent)
                .toList();

        return contentGroups.stream()
                .map(ContentGroup::getContent)
                .filter(content -> !reportedContents.contains(content))
                .toList();
    }

    private List<ContentReport> getContentReports(Member member) {
        return contentReportRepository.findAllByMember(member);
    }

    private Cursor getCursor(List<ContentGroup> contentGroups) {
        if (contentGroups.isEmpty()) {
            return new Cursor(null, null);
        }

        final ContentGroup lastContentGroup = contentGroups.get(contentGroups.size() - 1);

        return new Cursor(lastContentGroup.getId(), lastContentGroup.getCreatedAt());
    }

    private GroupSearchFeedResponse getGroupSearchFeedResponse(Cursor cursor, List<Content> contents) {
        final Group group = getGroup();
        return new GroupSearchFeedResponse(cursor.cursorId, cursor.cursorEndDate,
                contents.stream()
                        .map(content -> new GroupSearchContentResponse(
                                getMemberResponse(content.getMember()),
                                getContentResponse(content, group.getName())))
                        .toList());
    }

    private GroupSearchContentResponse.Member getMemberResponse(Member member) {
        return new GroupSearchContentResponse.Member(member.getId(), member.getMolluId(), member.getName(), member.getProfileSource());
    }

    private GroupSearchContentResponse.Content getContentResponse(Content content, String groupName) {
        return new GroupSearchContentResponse.Content(content.getId(), content.getLocation(), groupName, content.getMolluDateTime(),
                content.getCreatedAt(), content.getContentTag(),
                content.getFrontContentSource(), content.getBackContentSource());
    }

    // TODO: 로직 수정 및 테스트 코드 작성
    @Transactional
    @Override
    public String createContent(String memberId, CreateContentRequest request) {

        final Member member = getMember(memberId);
        final String frontContentFileUrl = uploadContent(request.frontContentFile());
        final String backContentFileUrl = uploadContent(request.backContentFile());
        final Content content = saveContent(request, frontContentFileUrl, backContentFileUrl, member);
        saveContentGroup(content);

        return content.getId();
    }

    private String uploadContent(MultipartFile file) {
        final ContentFile frontContentFile = new ImageContentFile(file, ContentType.CONTENTS);

        return fileHandler.upload(frontContentFile);
    }

    private Content saveContent(CreateContentRequest request, String frontContentFileUrl, String backContentFileUrl, Member member) {
        return contentRepository.save(Content.builder()
                .id(idConstructor.create())
                .location(request.location())
                .contentTag(request.tag())
                .question(request.question())
                .contentTime(ContentTime.of(request.molluDateTime(), request.uploadDateTime()))
                .contentSource(ContentSource.of(frontContentFileUrl, backContentFileUrl))
                .member(member)
                .build());
    }

    private void saveContentGroup(Content content) {
        final Group group = getGroup();
        contentGroupRepository.save(ContentGroup.builder()
                .id(idConstructor.create())
                .content(content)
                .group(group)
                .build());
    }

    private Group getGroup() {
        return groupRepository.findDefaultGroup()
                .orElseThrow(() -> new GroupNotFoundException("디폴트 그룹이 존재하지 않습니다."));
    }

    @Transactional
    @Override
    public void deleteContent(String memberId, String contentId) {

        final Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("[" + contentId + "] 는 존재하지 않는 컨텐츠입니다."));

        if (!content.isOwner(memberId)) {
            throw new MemberUnAuthorizedException("[" + memberId + "] 는 [" + contentId + "] 의 소유자가 아닙니다.");
        }

        contentRepository.delete(content);
    }

    private record Cursor(String cursorId, LocalDateTime cursorEndDate) {
    }
}
