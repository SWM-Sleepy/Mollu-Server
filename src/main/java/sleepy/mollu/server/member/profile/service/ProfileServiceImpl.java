package sleepy.mollu.server.member.profile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sleepy.mollu.server.content.domain.file.ContentType;
import sleepy.mollu.server.content.domain.file.ImageContentFile;
import sleepy.mollu.server.content.domain.handler.FileHandler;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.profile.dto.ProfileRequest;
import sleepy.mollu.server.member.profile.dto.ProfileResponse;
import sleepy.mollu.server.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;

    @Override
    public ProfileResponse searchProfile(String memberId) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);

        return new ProfileResponse(memberId, member.getMolluId(), member.getName(), member.getBirthday(),
                member.getProfileSource());
    }

    @Transactional
    @Override
    public void updateProfile(String memberId, ProfileRequest request) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);

        final String profileSource = getProfileSource(request.profileFile());
        member.updateProfile(request.molluId(), request.name(), request.birthday(), profileSource);
    }

    private String getProfileSource(MultipartFile profileFile) {
        if (profileFile != null) {
            final ImageContentFile contentFile = new ImageContentFile(profileFile, ContentType.PROFILES);
            return fileHandler.upload(contentFile);
        }

        return null;
    }

}
