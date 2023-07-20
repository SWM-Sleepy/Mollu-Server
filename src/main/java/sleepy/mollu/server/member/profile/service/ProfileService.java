package sleepy.mollu.server.member.profile.service;

import sleepy.mollu.server.member.profile.dto.ProfileRequest;
import sleepy.mollu.server.member.profile.dto.ProfileResponse;

public interface ProfileService {

    ProfileResponse searchProfile(String memberId);

    void updateProfile(String memberId, ProfileRequest request);
}
