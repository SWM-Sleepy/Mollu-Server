package sleepy.mollu.server.group.dto;

import java.util.List;

public record MyGroupResponse(List<Group> groups) {

    public record Group(String groupId, String groupName, int groupMemberCount) {
    }
}
