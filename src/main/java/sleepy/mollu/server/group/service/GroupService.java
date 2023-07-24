package sleepy.mollu.server.group.service;

import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;

public interface GroupService {

    GroupMemberSearchResponse searchGroupMembers(String memberId, String groupId);
}
