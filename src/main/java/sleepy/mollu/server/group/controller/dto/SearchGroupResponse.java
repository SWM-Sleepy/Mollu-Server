package sleepy.mollu.server.group.controller.dto;

public record SearchGroupResponse(String groupId, String name, String introduction, String profileSource, int memberCount) {
}
