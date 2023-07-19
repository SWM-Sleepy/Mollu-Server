package sleepy.mollu.server.member.preference.service;

import sleepy.mollu.server.member.preference.dto.PreferenceRequest;

public interface PreferenceService {

    void updatePreference(String memberId, PreferenceRequest request);
}
