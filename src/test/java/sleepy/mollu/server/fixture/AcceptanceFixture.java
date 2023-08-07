package sleepy.mollu.server.fixture;

import sleepy.mollu.server.member.dto.SignupRequest;

import java.time.LocalDate;

public class AcceptanceFixture {

    public static final LocalDate nowDate = LocalDate.now();

    public static final SignupRequest 회원가입_요청_데이터 = new SignupRequest("name", nowDate, "mollu");
}
