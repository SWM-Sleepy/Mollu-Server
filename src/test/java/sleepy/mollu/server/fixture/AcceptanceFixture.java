package sleepy.mollu.server.fixture;

import sleepy.mollu.server.content.comment.controller.dto.CreateCommentRequest;
import sleepy.mollu.server.content.reaction.controller.dto.CreateReactionRequest;
import sleepy.mollu.server.content.report.dto.ReportRequest;
import sleepy.mollu.server.member.dto.SignupRequest;
import sleepy.mollu.server.member.preference.dto.PhoneTokenRequest;

import java.time.LocalDate;

public class AcceptanceFixture {

    public static final LocalDate NOW_DATE = LocalDate.now();

    public static final SignupRequest 회원가입_요청_데이터 = new SignupRequest("name", NOW_DATE, "mollu");
    public static final SignupRequest 회원가입_요청_데이터2 = new SignupRequest("name", NOW_DATE, "mollu1");
    public static final PhoneTokenRequest 알림_토큰_설정_요청_데이터 = new PhoneTokenRequest("phoneToken", null);
    public static final ReportRequest 신고_요청_데이터 = new ReportRequest("신고 사유");
    public static final CreateReactionRequest 컨텐츠_반응_추가_요청_데이터 = new CreateReactionRequest("emoticon1");
    public static final CreateCommentRequest 댓글_등록_요청_데이터 = new CreateCommentRequest("김".repeat(200));
}
