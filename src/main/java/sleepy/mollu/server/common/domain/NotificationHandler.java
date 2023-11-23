package sleepy.mollu.server.common.domain;

import sleepy.mollu.server.member.domain.Member;

import java.util.List;

public interface NotificationHandler {

    void send(List<Member> members, String title, String body);
}
