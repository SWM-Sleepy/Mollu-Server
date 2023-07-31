package sleepy.mollu.server.content.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.domain.content.ContentSource;
import sleepy.mollu.server.content.domain.content.ContentTime;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.content.dto.CreateContentRequest;
import sleepy.mollu.server.content.dto.GroupSearchContentResponse;
import sleepy.mollu.server.content.dto.GroupSearchFeedResponse;
import sleepy.mollu.server.content.exception.ContentNotFoundException;
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

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final ContentGroupRepository contentGroupRepository;

    private final IdConstructor idConstructor;
    private final FileHandler fileHandler;

    // TODO: 로직 수정 및 테스트 코드 작성
    @Override
    public GroupSearchFeedResponse searchGroupFeed(String memberId, Pageable pageable) {

        final Member member = getMember(memberId);
        final List<Group> groups = getGroups(member);
        final List<Content> contents = getContents(pageable, groups);

        return getGroupSearchFeedResponse(member, contents);
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

    private List<Content> getContents(Pageable pageable, List<Group> groups) {
        final Page<ContentGroup> contentGroups = contentGroupRepository.findAllByGroups(groups, pageable);
        final List<ContentGroup> contentGroupsWithContent = contentGroupRepository.findAllWithContentByContentGroups(contentGroups.toList());

        return contentGroupsWithContent.stream()
                .map(ContentGroup::getContent)
                .toList();
    }

    private GroupSearchFeedResponse getGroupSearchFeedResponse(Member member, List<Content> contents) {
        return new GroupSearchFeedResponse(contents.stream()
                .map(content ->
                        new GroupSearchContentResponse(getMemberResponse(member), getContentResponse(content)))
                .toList());
    }

    private GroupSearchContentResponse.Member getMemberResponse(Member member) {
        return new GroupSearchContentResponse.Member(member.getId(), member.getMolluId(), member.getName(), member.getProfileSource());
    }

    private GroupSearchContentResponse.Content getContentResponse(Content content) {
        final Group group = getGroup();
        return new GroupSearchContentResponse.Content(content.getId(), content.getLocation(), group.getName(), content.getMolluDateTime(),
                content.getCreatedAt(), content.getContentTag(),
                content.getFrontContentSource(), content.getBackContentSource());
    }

    // TODO: 로직 수정 및 테스트 코드 작성
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
        final ContentFile frontContentFile = new ImageContentFile(file);

        return fileHandler.upload(frontContentFile);
    }

    private Content saveContent(CreateContentRequest request, String frontContentFileUrl, String backContentFileUrl, Member member) {
        return contentRepository.save(Content.builder()
                .id(idConstructor.create())
                .location(request.location())
                .contentTag(request.tag())
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

    @Override
    public void deleteContent(String memberId, String contentId) {

        final Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ContentNotFoundException("[" + contentId + "] 는 존재하지 않는 컨텐츠입니다."));

        if (!content.isOwner(memberId)) {
            throw new MemberUnAuthorizedException("[" + memberId + "] 는 [" + contentId + "] 의 소유자가 아닙니다.");
        }

        contentRepository.delete(content);
    }
}
