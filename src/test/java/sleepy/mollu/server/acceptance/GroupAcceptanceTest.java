package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class GroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 사용자는_그룹을_생성할_수_있다() {
        // given
        final String member1 = 회원가입_요청_및_응답("google");
        final String member2 = 다른_사람_회원가입_요청_및_응답("apple");

        // when
        final String groupId = 그룹을_생성한다(member1);
        final String code = 그룹의_초대_코드를_조회한다(member1, groupId);
        초대_코드로_그룹을_조회한다(member2, code.toLowerCase());

        final ExtractableResponse<Response> 초대_코드로_그룹_참여_응답 = 초대_코드로_그룹_참여_요청(member2, code.toLowerCase());

        // then
        assertThat(초대_코드로_그룹_참여_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private String 그룹을_생성한다(String accessToken) {
        final ExtractableResponse<Response> response = 그룹_생성_요청(accessToken);
        final String groupId = getGroupId(response);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(groupId).isNotNull()
        );

        return groupId;
    }

    private String 그룹의_초대_코드를_조회한다(String accessToken, String groupId) {
        final ExtractableResponse<Response> 그룹_초대_코드_조회_응답 = 그룹_초대_코드_조회_요청(accessToken, groupId);
        final SearchGroupCodeResponse response = toObject(그룹_초대_코드_조회_응답, SearchGroupCodeResponse.class);
        final String code = response.code();

        assertAll(
                () -> assertThat(그룹_초대_코드_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(code).isNotNull()
        );

        return code;
    }

    private void 초대_코드로_그룹을_조회한다(String accessToken, String code) {
        final ExtractableResponse<Response> 초대_코드로_그룹_조회_응답 = 초대_코드로_그룹_조회_요청(accessToken, code.toLowerCase());
        final SearchGroupResponse response = toObject(초대_코드로_그룹_조회_응답, SearchGroupResponse.class);

        assertAll(
                () -> assertThat(초대_코드로_그룹_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.memberCount()).isEqualTo(1)
        );
    }
}
