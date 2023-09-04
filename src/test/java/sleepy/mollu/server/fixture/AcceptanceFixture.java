package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.reaction.controller.dto.CreateReactionRequest;
import sleepy.mollu.server.content.report.dto.ReportRequest;
import sleepy.mollu.server.member.dto.SignupRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AcceptanceFixture {

    public static final LocalDate NOW_DATE = LocalDate.now();
    public static final LocalDateTime NOW = LocalDateTime.now();

    public static final SignupRequest 회원가입_요청_데이터 = new SignupRequest("name", NOW_DATE, "mollu");
    public static final SignupRequest 회원가입_요청_데이터2 = new SignupRequest("name", NOW_DATE, "mollu1");
    public static final ReportRequest 신고_요청_데이터 = new ReportRequest("신고 사유");
    public static final CreateReactionRequest 반응_요청_데이터 = new CreateReactionRequest("emotion1");
}
