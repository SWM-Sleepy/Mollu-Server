package sleepy.mollu.server.group.dto;

import java.util.List;

public record GroupMemberSearchResponse(List<GroupMemberResponse> groupMemberResponses) {

    public record GroupMemberResponse(String memberId, String memberName, String memberProfileSource) {
    }

}
