package sleepy.mollu.server.member.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.domain.Preference;
import sleepy.mollu.server.member.exception.MemberNotFoundException;
import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.exception.PreferenceNotFoundException;
import sleepy.mollu.server.member.preference.service.PreferenceService;
import sleepy.mollu.server.member.repository.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {

    private final MemberRepository memberRepository;

    @Override
    public void updatePreference(String memberId, PreferenceRequest request) {

        final Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("[" + memberId + "]에 해당하는 멤버가 없습니다."));

        final Preference preference = member.getPreference();
        if (preference == null) {
            throw new PreferenceNotFoundException("[" + memberId + "]와 연관된 알림 설정이 없습니다.");
        }

        preference.update(request.molluAlarm(), request.contentAlarm());
    }
}