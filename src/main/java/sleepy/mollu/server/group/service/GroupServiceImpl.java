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
import sleepy.mollu.server.group.domain.group.Code;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
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
        final Group group = saveGroup(request);
        final GroupMember groupMember = saveGroupMember(group, member);

        return getCreateGroupResponse(group, groupMember);
    }

    private Group saveGroup(CreateGroupRequest request) {
        return groupRepository.save(Group.builder()
                .id(idConstructor.create())
                .name(request.name())
                .introduction(request.introduction())
                .code(Code.generate())
                .groupProfileSource(getGroupProfileSource(request.imageFile()))
                .build());
    }

    private String getGroupProfileSource(MultipartFile file) {
        final ContentFile contentFile = getContentFile(file);
        return fileHandler.upload(contentFile);
    }

    private GroupMember saveGroupMember(Group group, Member member) {
        return groupMemberRepository.save(GroupMember.builder()
                .id(idConstructor.create())
                .group(group)
                .member(member)
                .role(GroupMemberRole.MEMBER)
                .build());
    }

    private ImageContentFile getContentFile(MultipartFile file) {
        if (file == null) {
            return null;
        }

        return new ImageContentFile(file, ContentType.GROUPS);
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
}
