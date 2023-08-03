package sleepy.mollu.server.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse.GroupMemberResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
import sleepy.mollu.server.group.groupmember.domain.GroupMembers;
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
public class GroupServiceImpl implements GroupService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

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
        return new MyGroupResponse(List.of(new MyGroupResponse.Group("groupId", "groupName")));
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

    private List<GroupMemberResponse> getGroupMembers(GroupMembers groupMembers) {
        return groupMembers.groupMembers().stream()
                .map(GroupMember::getMember)
                .map(member -> new GroupMemberResponse(member.getId(), member.getName(), member.getProfileSource()))
                .toList();
    }
}
