package sleepy.mollu.server.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.content.contentgroup.domain.ContentGroup;
import sleepy.mollu.server.content.contentgroup.repository.ContentGroupRepository;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.groupmember.repository.GroupMemberRepository;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberContentUnAuthorizedException;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final ContentGroupRepository contentGroupRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Override
    public void authorizeMemberForContent(Member member, Content content) {
        final List<Group> groups = contentGroupRepository.findAllByContent(content)
                .stream()
                .map(ContentGroup::getGroup)
                .toList();

        if (!groupMemberRepository.existsByMemberAndGroupIn(member, groups)) {
            throw new MemberContentUnAuthorizedException("해당 컨텐츠에 대한 접근 권한이 없습니다.");
        }
    }
}
