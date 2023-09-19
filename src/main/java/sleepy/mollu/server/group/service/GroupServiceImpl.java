package sleepy.mollu.server.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.common.domain.IdConstructor;
import sleepy.mollu.server.content.domain.file.ContentFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.group.controller.dto.CreateGroupRequest;
import sleepy.mollu.server.group.controller.dto.CreateGroupResponse;
import sleepy.mollu.server.group.controller.dto.CreateGroupResponse.GroupResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupResponse;
import sleepy.mollu.server.group.domain.group.Code;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.exception.MemberGroupUnAuthorizedException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.domain.GroupMemberRole;
import sleepy.mollu.server.group.groupmember.domain.GroupMembers;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.exception.MemberUnAuthorizedException;
import sleepy.mollu.server.member.repository.MemberRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final IdConstructor idConstructor;
    private final FileHandler fileHandler;

    @Override
    public GroupMemberSearchResponse searchGroupMembers(String memberId, String groupId) {

        final Member member = getMember(memberId);
        final Group group = getGroup(groupId);

        return getGroupMemberSearchResponse(group, member);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 멤버입니다."));
    }

    @Override
    public MyGroupResponse searchMyGroups(String memberId) {
        final Member member = getMember(memberId);
        final List<Group> groups = getGroups(member);

        return getMyGroupResponse(groups);
    }

    private List<Group> getGroups(Member member) {
        final List<GroupMember> groupMembers = groupMemberRepository.findAllWithGroupByMember(member);
        return groupMembers.stream()
                .map(GroupMember::getGroup)
                .toList();
    }

    private MyGroupResponse getMyGroupResponse(List<Group> groups) {
        return new MyGroupResponse(groups.stream()
                .map(group -> new MyGroupResponse.Group(group.getId(), group.getName(), getGroupMemberCount(group)))
                .toList());
    }

    private int getGroupMemberCount(Group group) {
        final List<GroupMember> allByGroup = groupMemberRepository.findAllByGroup(group);
        return allByGroup.size();
    }

    private Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("[" + groupId + "]는 존재하지 않는 그룹입니다."));
    }

    private GroupMemberSearchResponse getGroupMemberSearchResponse(Group group, Member member) {
        final GroupMembers groupMembers = new GroupMembers(groupMemberRepository.findAllWithMemberByGroup(group));
        validateGroupMember(groupMembers, member, group.getId());
        return new GroupMemberSearchResponse(getGroupMembers(groupMembers));
    }

    private void validateGroupMember(GroupMembers groupMembers, Member member, String groupId) {
        if (!groupMembers.hasMember(member)) {
            throw new MemberUnAuthorizedException("[" + member.getId() + "]는 [" + groupId + "] 그룹의 멤버가 아닙니다.");
        }
    }

    private List<GroupMemberSearchResponse.GroupMemberResponse> getGroupMembers(GroupMembers groupMembers) {
        return groupMembers.groupMembers().stream()
                .map(GroupMember::getMember)
                .map(member -> new GroupMemberSearchResponse.GroupMemberResponse(
                        member.getId(), member.getMolluId(), member.getName(), member.getProfileSource()))
                .toList();
    }

    @Transactional
    @Override
    public CreateGroupResponse createGroup(String memberId, CreateGroupRequest request) {

        final Member member = getMember(memberId);
        final Group group = createAndSaveGroup(request);
        final GroupMember groupMember = saveGroupMember(group, member);

        return getCreateGroupResponse(group, groupMember);
    }

    private Group createAndSaveGroup(CreateGroupRequest request) {
        return groupRepository.save(Group.builder()
                .id(idConstructor.create())
                .name(request.name())
                .introduction(request.introduction())
                .code(Code.generate())
                .groupProfileSource(uploadGroupProfile(request.imageFile()))
                .build());
    }

    private String uploadGroupProfile(MultipartFile file) {
        if (file != null) {
            final ContentFile contentFile = new ImageContentFile(file, ContentType.GROUPS);
            return fileHandler.upload(contentFile);
        }
        return null;
    }

    private GroupMember saveGroupMember(Group group, Member member) {
        return groupMemberRepository.save(GroupMember.builder()
                .id(idConstructor.create())
                .group(group)
                .member(member)
                .role(GroupMemberRole.MEMBER)
                .build());
    }

    private CreateGroupResponse getCreateGroupResponse(Group group, GroupMember groupMember) {
        return new CreateGroupResponse(getGroupResponse(group), getGroupMemberResponse(groupMember));
    }

    private GroupResponse getGroupResponse(Group group) {
        return new GroupResponse(
                group.getId(), group.getName(), group.getIntroduction(), group.getCode(), group.getGroupProfileSource());
    }

    private CreateGroupResponse.GroupMemberResponse getGroupMemberResponse(GroupMember groupMember) {
        return new CreateGroupResponse.GroupMemberResponse(
                groupMember.getId(), groupMember.getGroup().getId(), groupMember.getMember().getId());
    }

    @Override
    public SearchGroupCodeResponse searchGroupCode(String memberId, String groupId) {

        final Member member = getMember(memberId);
        final Group group = getGroup(groupId);
        checkMemberIsGroupMember(member, group);

        return new SearchGroupCodeResponse(group.getCode());
    }

    private void checkMemberIsGroupMember(Member member, Group group) {
        if (!groupMemberRepository.existsByMemberAndGroup(member, group)) {
            throw new MemberGroupUnAuthorizedException("[" + member.getId() + "]는 [" + group.getId() + "] 그룹의 멤버가 아닙니다.");
        }
    }

    @Override
    public SearchGroupResponse searchGroupByCode(String memberId, String code) {

        validateMember(memberId);
        final Group group = getGroupBy(code);
        final int memberCount = getMemberCount(group);

        return getSearchGroupResponse(group, memberCount);
    }

    private void validateMember(String memberId) {
        if (!memberRepository.existsById(memberId)) {
            throw new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 멤버입니다.");
        }
    }

    private Group getGroupBy(String code) {
        return groupRepository.findByCode_Value(code)
                .orElseThrow(() -> new GroupNotFoundException("[" + code + "]는 존재하지 않는 그룹 코드입니다."));
    }

    private int getMemberCount(Group group) {
        return groupMemberRepository.countByGroup(group);
    }

    private SearchGroupResponse getSearchGroupResponse(Group group, int memberCount) {
        return new SearchGroupResponse(
                group.getId(), group.getName(), group.getIntroduction(), group.getGroupProfileSource(), memberCount);
    }
}
