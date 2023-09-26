package sleepy.mollu.server.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.controller.dto.SearchContentResponse;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.content.ContentSource;
import sleepy.mollu.server.content.domain.content.ContentTime;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.content.domain.handler.dto.OriginThumbnail;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchContentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.content.report.repository.ContentReportRepository;
import sleepy.mollu.server.content.repository.ContentRepository;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.exception.GroupBadRequestException;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
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

    @Override
    public GroupSearchFeedResponse searchGroupFeed(String memberId, String cursorId, LocalDateTime cursorEndDate) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final FeedResponse feed = getFeed(member, cursorId, cursorEndDate);

        return getGroupSearchFeedResponse(feed.cursor, feed.contents);
    }

    private FeedResponse getFeed(Member member, String cursorId, LocalDateTime cursorEndDate) {
        final List<Group> groups = getGroups(member);
        final List<ContentGroup> contentGroups = contentGroupRepository.findGroupFeed(groups, PAGE_SIZE, cursorId, cursorEndDate);
        final List<Content> notReportedContents = getNotReportedContents(contentGroups, member);
        final Cursor cursor = getCursor(contentGroups);
        return new FeedResponse(notReportedContents, cursor);
    }

    private List<Group> getGroups(Member member) {
        final List<GroupMember> groupMembers = groupMemberRepository.findAllWithGroupByMember(member);
        return groupMembers.stream()
                .map(GroupMember::getGroup)
                .toList();
    }

    private List<Content> getNotReportedContents(List<ContentGroup> contentGroups, Member member) {
        final List<Content> reportedContents = getReportedContents(member);

        return contentGroups.stream()
                .map(ContentGroup::getContent)
                .filter(content -> !reportedContents.contains(content))
                .toList();
    }

    private List<Content> getReportedContents(Member member) {
        final List<ContentReport> contentReports = contentReportRepository.findAllByMember(member);
        return contentReports.stream()
                .map(ContentReport::getContent)
                .toList();
    }

    private Cursor getCursor(List<ContentGroup> contentGroups) {
        if (contentGroups.isEmpty()) {
            return new Cursor(null, null);
        }

        final ContentGroup lastContentGroup = contentGroups.get(contentGroups.size() - 1);

        return new Cursor(lastContentGroup.getId(), lastContentGroup.getCreatedAt());
    }

    private GroupSearchFeedResponse getGroupSearchFeedResponse(Cursor cursor, List<Content> contents) {
        return new GroupSearchFeedResponse(cursor.cursorId, cursor.cursorEndDate,
                contents.stream()
                        .map(content -> new GroupSearchContentResponse(
                                getMemberResponse(content.getMember()),
                                getContentResponse(content)))
                        .toList());
    }

    private GroupSearchContentResponse.Member getMemberResponse(Member member) {
        return new GroupSearchContentResponse.Member(member.getId(), member.getMolluId(), member.getName(), member.getProfileSource());
    }

    private GroupSearchContentResponse.Content getContentResponse(Content content) {
        return new GroupSearchContentResponse.Content(content.getId(), content.getLocation(), content.getMolluDateTime(),
                content.getCreatedAt(), content.getContentTag(),
                content.getFrontContentSource(), content.getBackContentSource());
    }

    @Transactional
    @Override
    public String createContent(String memberId, CreateContentRequest request) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final OriginThumbnail frontSource = uploadContent(request.frontContentFile());
        final OriginThumbnail backSource = uploadContent(request.backContentFile());
        final Content content = saveContent(request, frontSource, backSource, member);
        saveContentGroups(content, request.groups());

        return content.getId();
    }

    private OriginThumbnail uploadContent(MultipartFile file) {
        final ContentFile frontContentFile = new ImageContentFile(file, ContentType.CONTENTS);
        return fileHandler.uploadWithThumbnail(frontContentFile);
    }

    private Content saveContent(CreateContentRequest request, OriginThumbnail frontSource, OriginThumbnail backSource, Member member) {
        return contentRepository.save(Content.builder()
                .id(idConstructor.create())
                .location(request.location())
                .contentTag(request.tag())
                .question(request.question())
                .contentTime(ContentTime.of(request.molluDateTime(), request.uploadDateTime()))
                .contentSource(ContentSource.of(
                        frontSource.originSource(), backSource.originSource(),
                        frontSource.thumbnailSource(), backSource.thumbnailSource()))
                .member(member)
                .build());
    }

    private void saveContentGroups(Content content, List<String> groupIds) {
        if (groupIds == null) {
            final Group defaultGroup = getDefaultGroup();
            contentGroupRepository.save(createContentGroup(content, defaultGroup));
            return;
        }

        final List<Group> groups = getGroups(groupIds);
        contentGroupRepository.saveAll(groups.stream()
                .map(group -> createContentGroup(content, group))
                .toList());
    }

    private Group getDefaultGroup() {
        return groupRepository.findDefaultGroup()
                .orElseThrow(() -> new GroupNotFoundException("디폴트 그룹이 존재하지 않습니다."));
    }

    private List<Group> getGroups(List<String> groupIds) {
        final List<Group> groups = groupRepository.findByIdIn(groupIds);
        validateGroups(groups);
        return groups;
    }

    private void validateGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            throw new GroupBadRequestException("요청 받은 groupId로 그룹을 찾을 수 없습니다.");
        }
    }

    private ContentGroup createContentGroup(Content content, Group group) {
        return ContentGroup.builder()
                .id(idConstructor.create())
                .content(content)
                .group(group)
                .build();
    }

    @Transactional
    @Override
    public void deleteContent(String memberId, String contentId) {

        final Content content = contentRepository.findByIdOrElseThrow(contentId);

        if (!content.isOwner(memberId)) {
            throw new MemberUnAuthorizedException("[" + memberId + "] 는 [" + contentId + "] 의 소유자가 아닙니다.");
        }

        contentRepository.delete(content);
    }

    @Override
    public SearchContentResponse searchContent(String memberId, String contentId) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Content content = contentRepository.findByIdOrElseThrow(contentId);
        validateOwner(memberId, contentId, member, content);

        return getSearchContentResponse(contentId, content);
    }

    private void validateOwner(String memberId, String contentId, Member member, Content content) {
        if (!content.isOwner(member)) {
            throw new MemberUnAuthorizedException("[" + memberId + "] 는 [" + contentId + "] 의 소유자가 아닙니다.");
        }
    }

    private SearchContentResponse getSearchContentResponse(String contentId, Content content) {
        return new SearchContentResponse(
                contentId, content.getLocation(),
                content.getMolluDateTime(), content.getUploadDateTime(),
                content.getContentTag(), content.getFrontContentSource(), content.getBackContentSource());
    }

    private record Cursor(String cursorId, LocalDateTime cursorEndDate) {
    }

    private record FeedResponse(List<Content> contents, Cursor cursor) {
    }
}
