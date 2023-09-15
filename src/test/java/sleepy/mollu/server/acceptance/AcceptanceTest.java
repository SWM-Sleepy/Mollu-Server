package sleepy.mollu.server.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import sleepy.mollu.server.alarm.repository.MolluAlarmRepository;
import sleepy.mollu.server.config.TestConfig;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.oauth2.dto.CheckResponse;
import sleepy.mollu.server.oauth2.dto.TokenResponse;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.*;
import static sleepy.mollu.server.fixture.AcceptanceFixture.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestConfig.class)
public class AcceptanceTest {

    protected static final LocalDateTime NOW = LocalDateTime.now();
    private static final String BASE_URL = "/api";
    private static final String AUTH_URL = BASE_URL + "/auth";
    private static final String MEMBER_URL = BASE_URL + "/members";
    private static final String MEMBER_PREFERENCE_URL = MEMBER_URL + "/preference";
    private static final String GROUP_URL = BASE_URL + "/groups";
    private static final String MEMBER_EMOJI_URL = MEMBER_URL + "/emojis";
    private static final String CONTENT_URL = BASE_URL + "/contents";
    @LocalServerPort
    protected int port;
    @SpyBean
    protected Clock clock;
    @SpyBean
    protected MolluAlarmRepository molluAlarmRepository;
    @Autowired
    private DBCleaner dbCleaner;
    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void setUp() {
        if (RestAssured.port == RestAssured.UNDEFINED_PORT) {
            RestAssured.port = port;
            dbCleaner.afterPropertiesSet();
        }
        dbCleaner.clean();

        saveDefaultGroup();
    }

    private void saveDefaultGroup() {
        groupRepository.save(Group.builder()
                .id(UUID.randomUUID().toString())
                .name("디폴트 그룹")
                .introduction("디폴트 그룹입니다.")
                .groupProfileSource("")
                .build());
    }

    protected ExtractableResponse<Response> 로그인_요청(String type) {
        return post(AUTH_URL + "/login/" + type, "socialToken");
    }

    protected void 아이디_중복_확인_요청() {
        final CheckResponse response = toObject(get(AUTH_URL + "/check-id?molluId=molluId", "socialToken"), CheckResponse.class);
        assert response.isAvailable();
    }

    protected ExtractableResponse<Response> 다른_사람_회원가입_요청(String type) {
        return post(AUTH_URL + "/signup/" + type, "socialToken", 회원가입_요청_데이터2);
    }

    protected ExtractableResponse<Response> 회원가입_요청(String type) {
        return post(AUTH_URL + "/signup/" + type, "socialToken", 회원가입_요청_데이터);
    }

    protected ExtractableResponse<Response> 토큰_재발급_요청(String refreshToken) {
        return post(AUTH_URL + "/refresh", refreshToken);
    }

    protected ExtractableResponse<Response> 회원탈퇴_요청(String accessToken) {
        return delete(MEMBER_URL, accessToken);
    }

    protected ExtractableResponse<Response> 알림_토큰_설정_요청(String accessToken) {
        return post(MEMBER_PREFERENCE_URL + "/token", accessToken, 알림_토큰_설정_요청_데이터);
    }

    protected ExtractableResponse<Response> 프로필_조회_요청(String accessToken) {
        return get(MEMBER_URL + "/profile", accessToken);
    }

    protected ExtractableResponse<Response> MOLLU_타임_조회_요청(String accessToken) {
        return get(CONTENT_URL + "/mollu-time", accessToken);
    }

    protected ExtractableResponse<Response> 그룹_생성_요청(String accessToken) {
        return thenExtract(RestAssured.given()
                .headers(Map.of("Authorization", "Bearer " + accessToken))
                .multiPart("name", "그룹명")
                .when()
                .post(GROUP_URL));
    }

    protected ExtractableResponse<Response> 컨텐츠_업로드_요청(String accessToken, LocalDateTime uploadDateTime) {
        return thenExtract(RestAssured.given()
                .headers(Map.of("Authorization", "Bearer " + accessToken))
                .multiPart("location", "location")
                .multiPart("tag", "tag")
                .multiPart("uploadDateTime", uploadDateTime.toString())
                .multiPart("frontContentFile", "test_file.jpg", "Something".getBytes(), MediaType.IMAGE_PNG_VALUE)
                .multiPart("backContentFile", "test_file.jpg", "Something".getBytes(), MediaType.IMAGE_PNG_VALUE)
                .when()
                .post(CONTENT_URL));
    }

    protected ExtractableResponse<Response> 컨텐츠_업로드_요청(String accessToken) {
        return 컨텐츠_업로드_요청(accessToken, NOW);
    }

    protected ExtractableResponse<Response> 그룹원_피드_조회_요청(String accessToken) {
        return get(CONTENT_URL + "/group", accessToken);
    }

    protected ExtractableResponse<Response> 컨텐츠_신고_요청(String accessToken, String contentId) {
        return post(CONTENT_URL + "/" + contentId + "/report", accessToken, 신고_요청_데이터);
    }

    protected ExtractableResponse<Response> 내_이모티콘_등록_요청(String accessToken, String emojiType) {
        return thenExtract(RestAssured.given()
                .headers(Map.of("Authorization", "Bearer " + accessToken))
                .multiPart("emoji", emojiType)
                .multiPart("emojiFile", "test_file.jpg", "Something".getBytes(), MediaType.IMAGE_PNG_VALUE)
                .when()
                .post(MEMBER_EMOJI_URL));
    }

    protected ExtractableResponse<Response> 내_이모티콘_조회_요청(String accessToken) {
        return get(MEMBER_EMOJI_URL, accessToken);
    }

    protected ExtractableResponse<Response> 내_이모티콘_삭제_요청(String accessToken, String emojiType) {
        return delete(MEMBER_EMOJI_URL + "?emoji=" + emojiType, accessToken);
    }

    protected ExtractableResponse<Response> 컨텐츠_반응_추가_요청(String accessToken, String contentId) {
        return post(CONTENT_URL + "/" + contentId + "/reactions", accessToken, 컨텐츠_반응_추가_요청_데이터);
    }

    protected ExtractableResponse<Response> 컨텐츠_반응_여부_조회_요청(String accessToken, String contentId) {
        return get(CONTENT_URL + "/" + contentId + "/reactions/my", accessToken);
    }

    protected ExtractableResponse<Response> 컨텐츠_반응_조회_요청(String accessToken, String contentId) {
        return get(CONTENT_URL + "/" + contentId + "/reactions", accessToken);
    }

    protected ExtractableResponse<Response> 컨텐츠_반응_삭제_요청(String accessToken, String contentId, String reactionId) {
        return delete(CONTENT_URL + "/" + contentId + "/reactions/" + reactionId, accessToken);
    }

    protected ExtractableResponse<Response> 댓글_등록_요청(String accessToken, String contentId) {
        return post(CONTENT_URL + "/" + contentId + "/comments", accessToken, 댓글_등록_요청_데이터);
    }

    protected ExtractableResponse<Response> 댓글_조회_요청(String accessToken, String contentId) {
        return get(CONTENT_URL + "/" + contentId + "/comments", accessToken);
    }

    protected ExtractableResponse<Response> 댓글_삭제_요청(String accessToken, String contentId, String commentId) {
        return delete(CONTENT_URL + "/" + contentId + "/comments/" + commentId, accessToken);
    }

    protected ExtractableResponse<Response> 댓글_신고_요청(String accessToken, String contentId, String commentId) {
        return post(CONTENT_URL + "/" + contentId + "/comments/" + commentId + "/report", accessToken);
    }

    protected String 회원가입_요청_및_응답(String type) {
        final ExtractableResponse<Response> 회원가입_응답 = 회원가입_요청(type);
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        return response.accessToken();
    }

    protected String 다른_사람_회원가입_요청_및_응답(String type) {
        final ExtractableResponse<Response> 회원가입_응답 = 다른_사람_회원가입_요청(type);
        final TokenResponse response = toObject(회원가입_응답, TokenResponse.class);
        return response.accessToken();
    }

    protected String getContentId(ExtractableResponse<Response> response) {
        return getLocation(response).split("/")[2];
    }

    protected String getReactionId(ExtractableResponse<Response> response) {
        return getLocation(response).split("/")[4];
    }

    protected String getCommentId(ExtractableResponse<Response> response) {
        return getLocation(response).split("/")[4];
    }

    protected String getGroupId(ExtractableResponse<Response> response) {
        return getLocation(response).split("/")[2];
    }
}
