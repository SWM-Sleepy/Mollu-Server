package sleepy.mollu.server.admin.controller.model;

import sleepy.mollu.server.member.domain.Platform;

public record UserModel(String memberId, String molluId, String name, String profileSource, String phoneToken, Platform platform) {
}
