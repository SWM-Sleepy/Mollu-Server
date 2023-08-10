package sleepy.mollu.server.group.dto;

import java.util.List;

public record GroupMemberSearchResponse(List<GroupMemberResponse> groupMembers) {

    public record GroupMemberResponse(String memberId, String molluId, String name, String profileSource) {
    }

}
