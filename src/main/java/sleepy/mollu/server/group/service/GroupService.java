package sleepy.mollu.server.group.service;

import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;

public interface GroupService {

    GroupMemberSearchResponse searchGroupMembers(String memberId, String groupId);

    MyGroupResponse searchMyGroups(String memberId);
}
