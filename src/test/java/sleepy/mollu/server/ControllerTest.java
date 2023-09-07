package sleepy.mollu.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import sleepy.mollu.server.alarm.admin.service.AdminAlarmService;
import sleepy.mollu.server.alarm.admin.service.AdminService;
import sleepy.mollu.server.content.mollutime.service.MolluTimeService;
import sleepy.mollu.server.content.reaction.service.ContentReactionService;
import sleepy.mollu.server.content.report.service.ReportService;
import sleepy.mollu.server.content.service.ContentService;
import sleepy.mollu.server.group.service.GroupService;
import sleepy.mollu.server.member.emoji.service.MemberEmojiService;
import sleepy.mollu.server.member.preference.service.PreferenceService;
import sleepy.mollu.server.member.profile.service.ProfileService;
import sleepy.mollu.server.member.service.MemberService;
import sleepy.mollu.server.oauth2.jwt.config.JwtConfig;
import sleepy.mollu.server.oauth2.jwt.dto.JwtToken;
import sleepy.mollu.server.oauth2.jwt.service.JwtGenerator;
import sleepy.mollu.server.oauth2.service.OAuth2Service;

import java.util.Arrays;

@WebMvcTest
@Import(JwtConfig.class)
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtGenerator jwtGenerator;
    @MockBean
    protected ReportService reportService;
    @MockBean
    protected ContentService contentService;
    @MockBean
    protected OAuth2Service oauth2Service;
    @MockBean
    protected GroupService groupService;
    @MockBean
    protected ProfileService profileService;
    @MockBean
    protected PreferenceService preferenceService;
    @MockBean
    protected MolluTimeService molluTimeService;
    @MockBean
    protected MemberEmojiService memberEmojiService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private AdminService adminService;
    @MockBean
    private AdminAlarmService adminAlarmService;
    @MockBean
    private ContentReactionService contentReactionService;

    private static HttpHeaders getHeaders(String accessToken) {
        final HttpHeaders headers = new HttpHeaders();
        if (accessToken != null) {
            headers.add("Authorization", "Bearer " + accessToken);
        }
        return headers;
    }

    protected ResultActions get(String path, MockHttpSession session) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path)
                .session(session));
    }

    protected ResultActions get(String path, String accessToken) throws Exception {

        final HttpHeaders headers = getHeaders(accessToken);

        return mockMvc.perform(MockMvcRequestBuilders.get(path)
                .headers(headers));
    }

    protected ResultActions get(String path) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(path));
    }

    protected ResultActions post(String path, String accessToken, Object requestBody) throws Exception {

        final HttpHeaders headers = getHeaders(accessToken);
        final String body = objectMapper.writeValueAsString(requestBody);

        return mockMvc.perform(MockMvcRequestBuilders.post(path)
                .headers(headers)
                .contentType("application/json")
                .content(body));
    }

    protected ResultActions post(String path, Object requestBody) throws Exception {

        return post(path, getAccessToken("memberId"), requestBody);
    }

    protected ResultActions post(String path, String accessToken) throws Exception {

        return post(path, accessToken, null);
    }

    protected ResultActions post(String path) throws Exception {
        return post(path, getAccessToken("memberId"));
    }

    protected ResultActions put(String path, String accessToken, Object requestBody) throws Exception {

        final HttpHeaders headers = getHeaders(accessToken);
        final String body = objectMapper.writeValueAsString(requestBody);

        return mockMvc.perform(MockMvcRequestBuilders.put(path)
                .headers(headers)
                .contentType("application/json")
                .content(body));
    }

    protected ResultActions put(String path, String accessToken) throws Exception {

        return put(path, accessToken, null);
    }

    protected ResultActions put(String path) throws Exception {
        return put(path, null);
    }

    protected ResultActions delete(String path, String accessToken) throws Exception {

        final HttpHeaders headers = getHeaders(accessToken);

        return mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .headers(headers));
    }

    protected ResultActions delete(String path) throws Exception {
        return delete(path, null);
    }

    protected ResultActions multipart(HttpMethod method, String path, String[] files, String accessToken, MultiValueMap<String, String> params) throws Exception {

        final HttpHeaders headers = getHeaders(accessToken);

        final MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart(method, path);

        if (files != null) {
            Arrays.stream(files).forEach(file -> builder.file(getMockMultipartFile(file)));
        }

        builder.headers(headers);

        if (params != null) {
            builder.params(params);
        }

        return mockMvc.perform(builder);
    }

    protected ResultActions multipart(HttpMethod method, String path, String accessToken, MultiValueMap<String, String> params) throws Exception {

        return multipart(method, path, null, accessToken, params);
    }

    protected ResultActions multipart(HttpMethod method, String path, String[] files, String accessToken) throws Exception {

        return multipart(method, path, files, accessToken, null);
    }

    protected String getAccessToken(String memberId) {
        final JwtToken jwtToken = jwtGenerator.generate(memberId);
        return jwtToken.accessToken();
    }

    protected String getRefreshToken(String memberId) {
        final JwtToken jwtToken = jwtGenerator.generate(memberId);
        return jwtToken.refreshToken();
    }

    private MockMultipartFile getMockMultipartFile(String contentFile) {
        return new MockMultipartFile(contentFile, "test_file.png",
                "image/png", "Spring Framework".getBytes());
    }
}
