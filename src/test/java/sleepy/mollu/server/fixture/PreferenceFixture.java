package sleepy.mollu.server.fixture;

import sleepy.mollu.server.member.domain.Preference;

public class PreferenceFixture {

    public static Preference create() {
        return Preference.builder()
                .molluAlarm(true)
                .contentAlarm(true)
                .build();
    }
}
