package sleepy.mollu.server.member.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.profile.dto.ProfileResponse;
import sleepy.mollu.server.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final MemberRepository memberRepository;

    @Override
    public ProfileResponse searchProfile(String memberId) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("ID가 [" + memberId + "]인 멤버를 찾을 수 없습니다."));

        return new ProfileResponse(memberId, member.getMolluId(), member.getName(), member.getBirthday());
    }
}
