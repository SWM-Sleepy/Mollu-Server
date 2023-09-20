package sleepy.mollu.server.member.preference.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Platform;
import sleepy.mollu.server.member.domain.Preference;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.dto.PreferenceResponse;
import sleepy.mollu.server.member.preference.exception.PreferenceNotFoundException;
import sleepy.mollu.server.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void updatePreference(String memberId, PreferenceRequest request) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Preference preference = member.getPreference();
        if (preference == null) {
            throw new PreferenceNotFoundException("[" + memberId + "]와 연관된 알림 설정이 없습니다.");
        }

        preference.update(request.molluAlarm(), request.contentAlarm());
    }

    @Override
    public PreferenceResponse searchPreference(String memberId) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        final Preference preference = member.getPreference();
        if (preference == null) {
            throw new PreferenceNotFoundException("[" + memberId + "]와 연관된 알림 설정이 없습니다.");
        }

        return new PreferenceResponse(preference.isMolluAlarm(), preference.isContentAlarm());
    }

    @Transactional
    @Override
    public void updatePhoneToken(String memberId, String phoneToken, String platform) {

        final Member member = memberRepository.findByIdOrElseThrow(memberId);
        member.updatePhoneToken(phoneToken, Platform.from(platform));
    }
}
