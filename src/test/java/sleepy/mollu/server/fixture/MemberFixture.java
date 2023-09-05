package sleepy.mollu.server.fixture;

import sleepy.mollu.server.member.domain.Member;

import java.time.LocalDate;

public class MemberFixture {

    public static final String DEFAULT_NAME = "name";
    public static final LocalDate DEFAULT_BIRTHDAY = LocalDate.now();

    public static Member create(String id, String molluId) {
        return Member.builder()
                .id(id)
                .molluId(molluId)
                .name(DEFAULT_NAME)
                .birthday(DEFAULT_BIRTHDAY)
                .preference(PreferenceFixture.create())
                .build();
    }

    public static final Member 멤버1 = create("memberId1", "mollu_id1");
    public static final Member 멤버2 = create("memberId2", "mollu_id2");
}
