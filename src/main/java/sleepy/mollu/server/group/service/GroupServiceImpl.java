package sleepy.mollu.server.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse.GroupMemberResponse;
import sleepy.mollu.server.group.exception.GroupNotFoundException;
import sleepy.mollu.server.group.groupmember.domain.GroupMember;
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

    // TODO: 2023/07/24 그룹 조회시 fetch join을 하여 N+1 문제 해결하기
    @Override
    public GroupMemberSearchResponse searchGroupMembers(String memberId, String groupId) {

        final Member member = getMember(memberId);
        final Group group = getGroup(groupId);
        validateGroupMember(member, group);

        return getGroupMemberSearchResponse(group);
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]는 존재하지 않는 멤버입니다."));
    }

    private Group getGroup(String groupId) {
        return groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("[" + groupId + "]는 존재하지 않는 그룹입니다."));
    }

    private void validateGroupMember(Member member, Group group) {
        if (!group.hasMember(member)) {
            throw new MemberUnAuthorizedException("[" + member.getId() + "]는 [" + group.getId() + "]의 그룹원이 아닙니다.");
        }
    }

    private GroupMemberSearchResponse getGroupMemberSearchResponse(Group group) {
        return new GroupMemberSearchResponse(getGroupMembers(group));
    }

    private List<GroupMemberResponse> getGroupMembers(Group group) {
        return group.getGroupMembers().stream()
                .map(GroupMember::getMember)
                .map(member -> new GroupMemberResponse(member.getId(), member.getName(), member.getProfileSource()))
                .toList();
    }
}
