package sleepy.mollu.server.group.controller.dto;

public record CreateGroupResponse(GroupResponse groupResponse, GroupMemberResponse groupMemberResponse) {

    public record GroupResponse(String id, String name, String introduction, String code, String groupProfileSource) {
    }

    public record GroupMemberResponse(String id, String group, String member) {
    }

}
