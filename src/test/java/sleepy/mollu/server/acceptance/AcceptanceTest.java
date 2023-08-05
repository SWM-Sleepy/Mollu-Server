package sleepy.mollu.server.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import sleepy.mollu.server.config.TestConfig;
import sleepy.mollu.server.group.domain.group.Group;
import sleepy.mollu.server.group.repository.GroupRepository;
import sleepy.mollu.server.oauth2.dto.CheckResponse;

import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.*;
import static sleepy.mollu.server.fixture.AcceptanceFixture.회원가입_요청_데이터;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestConfig.class)
public class AcceptanceTest {

    private static final String BASE_URL = "/api";
    private static final String AUTH_URL = BASE_URL + "/auth";

    @LocalServerPort
    protected int port;

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

    protected ExtractableResponse<Response> 회원가입_요청(String type) {
        return post(AUTH_URL + "/signup/" + type, "socialToken", 회원가입_요청_데이터);
    }

    protected ExtractableResponse<Response> 토큰_재발급_요청(String refreshToken) {
        return post(AUTH_URL + "/refresh", refreshToken);
    }
}
