package sleepy.mollu.server.fixture;

import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDate;
import java.util.UUID;

public class MemberFixture {

    public static final String DEFAULT_ID = "memberId";
    public static final String DEFAULT_NAME = "name";
    public static final LocalDate DEFAULT_BIRTHDAY = LocalDate.now();

    public static Member create() {
        return Member.builder()
                .id(DEFAULT_ID)
                .molluId(UUID.randomUUID().toString().substring(0, 20))
                .name(DEFAULT_NAME)
                .birthday(DEFAULT_BIRTHDAY)
                .preference(PreferenceFixture.create())
                .build();
    }

    public static Member create(String id, String molluId) {
        return Member.builder()
                .id(id)
                .molluId(molluId)
                .name(DEFAULT_NAME)
                .birthday(DEFAULT_BIRTHDAY)
                .preference(PreferenceFixture.create())
                .build();
    }
}
