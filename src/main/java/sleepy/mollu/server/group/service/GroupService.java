package sleepy.mollu.server.group.service;

import sleepy.mollu.server.group.controller.dto.CreateGroupRequest;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupResponse;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;

public interface GroupService {

    GroupMemberSearchResponse searchGroupMembers(String memberId, String groupId);

    MyGroupResponse searchMyGroups(String memberId);

    String createGroup(String memberId, CreateGroupRequest request);

    SearchGroupCodeResponse searchGroupCode(String memberId, String groupId);

    SearchGroupResponse searchGroupByCode(String memberId, String code);

    void joinGroupByCode(String memberId, String code);

    void leaveGroup(String memberId, String groupId);
}
