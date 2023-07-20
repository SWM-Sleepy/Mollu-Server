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

import java.time.LocalDate;

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

        updateMolluId(member, request.molluId());
        updateName(member, request.name());
        updateBirthday(member, request.birthday());
        updateProfile(member, request.profileFile());
    }

    private void updateMolluId(Member member, String molluId) {
        if (molluId != null) {
            member.updateMolluId(molluId);
        }
    }

    private void updateName(Member member, String name) {
        if (name != null) {
            member.updateName(name);
        }
    }

    private void updateBirthday(Member member, LocalDate birthday) {
        if (birthday != null) {
            member.updateBirthday(birthday);
        }
    }

    private void updateProfile(Member member, MultipartFile profileFile) {
        if (profileFile != null) {
            final ImageContentFile contentFile = new ImageContentFile(profileFile);
            final String profileFileUrl = fileHandler.upload(contentFile);
            member.updateProfileSource(profileFileUrl);
        }
    }

}
