package sleepy.mollu.server.content.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.domain.content.Content;
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
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;
    private final GroupRepository groupRepository;

    private final IdConstructor idConstructor;
    private final FileHandler fileHandler;

    // TODO: 로직 수정 및 테스트 코드 작성
    @Override
    public GroupSearchFeedResponse searchGroupFeed(String memberId, Pageable pageable) {

        final Member member = getMember(memberId);
        final Page<Content> contents = contentRepository.findAll(pageable);

        return getGroupSearchFeedResponse(member, contents);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "] 는 존재하지 않는 회원입니다."));
    }

    private GroupSearchFeedResponse getGroupSearchFeedResponse(Member member, Page<Content> contents) {

        return new GroupSearchFeedResponse(contents.stream()
                .map(content ->
                        new GroupSearchContentResponse(getMemberResponse(member), getContentResponse(content))
                ).toList());
    }

    private GroupSearchContentResponse.Member getMemberResponse(Member member) {
        return new GroupSearchContentResponse.Member(member.getId(), member.getMolluId(), member.getName(), member.getProfileSource());
    }

    private GroupSearchContentResponse.Content getContentResponse(Content content) {
        final String groupName = "groupName";
        final LocalDateTime limitDateTime = LocalDateTime.now();
        return new GroupSearchContentResponse.Content(content.getId(), content.getLocation(), groupName, limitDateTime,
                content.getCreatedAt(), content.getContentTag(),
                content.getFrontContentSource(), content.getBackContentSource());
    }

    // TODO: 로직 수정 및 테스트 코드 작성
    @Override
    public String createContent(String memberId, CreateContentRequest request) {

        final Member member = getMember(memberId);
        final String frontContentFileUrl = uploadContent(request.frontContentFile());
        final String backContentFileUrl = uploadContent(request.backContentFile());
        final ContentGroup contentGroup = getContentGroup();

        return saveContent(request, frontContentFileUrl, backContentFileUrl, member, contentGroup).getId();
    }

    private ContentGroup getContentGroup() {
        final Group group = getGroup();
        return ContentGroup.builder()
                .id(idConstructor.create())
                .group(group)
                .build();
    }

    private Group getGroup() {
        return groupRepository.findDefaultGroup()
                .orElseThrow(() -> new GroupNotFoundException("디폴트 그룹이 존재하지 않습니다."));
    }

    private Content saveContent(CreateContentRequest request, String frontContentFileUrl, String backContentFileUrl, Member member, ContentGroup contentGroup) {
        return contentRepository.save(Content.builder()
                .id(idConstructor.create())
                .location(request.location())
                .contentTag(request.tag())
                .frontContentSource(frontContentFileUrl)
                .backContentSource(backContentFileUrl)
                .member(member)
                .contentGroup(contentGroup)
                .build());
    }

    private String uploadContent(MultipartFile file) {
        final ContentFile frontContentFile = new ImageContentFile(file);

        return fileHandler.upload(frontContentFile);
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
