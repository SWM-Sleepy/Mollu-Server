package sleepy.mollu.server.member.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.profile.dto.ProfileRequest;
import sleepy.mollu.server.member.profile.dto.ProfileResponse;
import sleepy.mollu.server.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;

    @Override
    public ProfileResponse searchProfile(String memberId) {

        final Member member = getMember(memberId);

        return new ProfileResponse(memberId, member.getMolluId(), member.getName(), member.getBirthday(),
                member.getProfileSource());
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("ID가 [" + memberId + "]인 멤버를 찾을 수 없습니다."));
    }

    @Override
    public void updateProfile(String memberId, ProfileRequest request) {

        final Member member = getMember(memberId);

        final String profileSource = getProfileSource(request.profileFile());
        member.updateProfile(request.molluId(), request.name(), request.birthday(), profileSource);
    }

    private String getProfileSource(MultipartFile profileFile) {
        if (profileFile != null) {
            final ImageContentFile contentFile = new ImageContentFile(profileFile);
            return fileHandler.upload(contentFile);
        }

        return null;
    }

}
