package sleepy.mollu.server.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static sleepy.mollu.server.acceptance.SimpleRestAssured.toObject;

class GroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 사용자는_그룹을_생성할_수_있다() {
        // given
        final String accessToken = 회원가입_요청_및_응답("google");

        // when
        String groupId = 그룹을_생성한다(accessToken);
        final ExtractableResponse<Response> 그룹_초대_코드_조회_응답 = 그룹_초대_코드_조회_요청(accessToken, groupId);

        // then
        final SearchGroupCodeResponse response = toObject(그룹_초대_코드_조회_응답, SearchGroupCodeResponse.class);
        assertAll(
                () -> assertThat(그룹_초대_코드_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.code()).isNotNull()
        );
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
}
