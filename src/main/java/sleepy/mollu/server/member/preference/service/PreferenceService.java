package sleepy.mollu.server.member.preference.service;

import sleepy.mollu.server.member.preference.dto.PreferenceRequest;
import sleepy.mollu.server.member.preference.dto.PreferenceResponse;

public interface PreferenceService {

    void updatePreference(String memberId, PreferenceRequest request);

    PreferenceResponse searchPreference(String memberId);

    void updatePhoneToken(String memberId, String phoneToken, String platform);
}
