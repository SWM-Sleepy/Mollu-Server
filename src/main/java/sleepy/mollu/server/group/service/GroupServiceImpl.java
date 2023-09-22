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
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.member.domain.Member;
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

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Group group = getGroup(groupId);
        checkMemberIsGroupMember(member, group);

        return getGroupMemberSearchResponse(group);
    }

    private Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("[" + groupId + "]는 존재하지 않는 그룹입니다."));
    }

    private void checkMemberIsGroupMember(Member member, Group group) {
        if (!groupMemberRepository.existsByMemberAndGroup(member, group)) {
            throw new MemberGroupUnAuthorizedException("[" + member.getId() + "]는 [" + group.getId() + "] 그룹의 멤버가 아닙니다.");
        }
    }

    @Override
    public MyGroupResponse searchMyGroups(String memberId) {
        final Member member = memberRepository.findByIdOrElseThrow(memberId);
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

    private GroupMemberSearchResponse getGroupMemberSearchResponse(Group group) {
        final List<GroupMember> groupMembers = groupMemberRepository.findAllWithMemberByGroup(group);
        return new GroupMemberSearchResponse(getGroupMembers(groupMembers));
    }

    private List<GroupMemberSearchResponse.GroupMemberResponse> getGroupMembers(List<GroupMember> groupMembers) {
        return groupMembers.stream()
                .map(GroupMember::getMember)
                .map(member -> new GroupMemberSearchResponse.GroupMemberResponse(
                        member.getId(), member.getMolluId(), member.getName(), member.getProfileSource()))
                .toList();
    }

    @Transactional
    @Override
    public String createGroup(String memberId, CreateGroupRequest request) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Group group = createAndSaveGroup(request);
        saveGroupMember(group, member);

        return group.getId();
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

    private void saveGroupMember(Group group, Member member) {
        groupMemberRepository.save(GroupMember.builder()
                .id(idConstructor.create())
                .group(group)
                .member(member)
                .role(GroupMemberRole.MEMBER)
                .build());
    }

    @Override
    public SearchGroupCodeResponse searchGroupCode(String memberId, String groupId) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Group group = getGroup(groupId);
        checkMemberIsGroupMember(member, group);

        return new SearchGroupCodeResponse(group.getCode());
    }

    @Override
    public SearchGroupResponse searchGroupByCode(String memberId, String code) {

        memberRepository.findByIdOrElseThrow(memberId);
        final Group group = getGroupBy(code);
        final int memberCount = getMemberCount(group);

        return getSearchGroupResponse(group, memberCount);
    }

    private Group getGroupBy(String code) {
        return groupRepository.findByCode_Value(code.toUpperCase())
                .orElseThrow(() -> new GroupNotFoundException("[" + code + "]는 존재하지 않는 그룹 코드입니다."));
    }

    private int getMemberCount(Group group) {
        return groupMemberRepository.countByGroup(group);
    }

    private SearchGroupResponse getSearchGroupResponse(Group group, int memberCount) {
        return new SearchGroupResponse(
                group.getId(), group.getName(), group.getIntroduction(), group.getGroupProfileSource(), memberCount);
    }

    @Transactional
    @Override
    public void joinGroupByCode(String memberId, String code) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Group group = getGroupBy(code);
        saveGroupMember(member, group);
    }

    private void saveGroupMember(Member member, Group group) {
        groupMemberRepository.save(GroupMember.builder()
                .id(idConstructor.create())
                .member(member)
                .group(group)
                .role(GroupMemberRole.MEMBER)
                .build());
    }

    @Transactional
    @Override
    public void leaveGroup(String memberId, String groupId) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Group group = getGroup(groupId);
        final GroupMember groupMember = getGroupMember(member, group);

        deleteGroupMember(groupMember);
    }

    private GroupMember getGroupMember(Member member, Group group) {
        return groupMemberRepository.findByMemberAndGroup(member, group)
                .orElseThrow(() -> new MemberGroupUnAuthorizedException("[" + member.getId() + "]는 [" + group.getId() + "] 그룹의 멤버가 아닙니다."));
    }

    private void deleteGroupMember(GroupMember groupMember) {
        groupMemberRepository.delete(groupMember);
    }
}
