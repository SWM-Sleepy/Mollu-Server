package sleepy.mollu.server.member.service;

import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.member.domain.Member;

public interface AuthorizationService {


    void authorizeMemberForContent(Member member, Content content);
}
