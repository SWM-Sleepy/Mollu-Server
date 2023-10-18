package sleepy.mollu.server.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sleepy.mollu.server.admin.controller.model.CommentReportModel;
import sleepy.mollu.server.admin.controller.model.ContentModel;
import sleepy.mollu.server.admin.controller.model.ContentReportModel;
import sleepy.mollu.server.admin.controller.model.UserModel;
import sleepy.mollu.server.admin.service.AdminService;
import sleepy.mollu.server.content.comment.domain.Comment;
import sleepy.mollu.server.content.domain.content.Content;
import sleepy.mollu.server.content.report.domain.CommentReport;
import sleepy.mollu.server.content.report.domain.ContentReport;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.oauth2.jwt.dto.JwtToken;
import sleepy.mollu.server.oauth2.jwt.service.JwtGenerator;
import sleepy.mollu.server.swagger.CreatedResponse;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "관리자")
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtGenerator jwtGenerator;

    @Operation(summary = "관리자 로그인 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/login")
    public String getLogin() {

        return "login";
    }

    @Operation(summary = "관리자 로그인 요청")
    @CreatedResponse
    @InternalServerErrorResponse
    @PostMapping("/login")
    public String postLogin(@RequestParam String id, @RequestParam String password, HttpServletResponse response) throws NoSuchAlgorithmException {

        if (adminService.isAdmin(id, password)) {
            final JwtToken jwtToken = jwtGenerator.generate("admin");
            final Cookie cookie = new Cookie("admin", jwtToken.accessToken());
            cookie.setMaxAge(60 * 60);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "redirect:/admin/users";
        }

        return "redirect:/admin/login";
    }

    @Operation(summary = "사용자 조회 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/users")
    public String getUsers(Model model) {
        final List<Member> members = adminService.getMembers();
        final List<UserModel> users = members.stream()
                .map(member -> new UserModel(
                        member.getId(),
                        member.getMolluId(),
                        member.getName(),
                        member.getProfileSource(),
                        member.getPhoneToken(),
                        member.getPlatform())
                )
                .toList();
        model.addAttribute("users", users);

        return "users";
    }

    @Operation(summary = "컨텐츠 조회 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/contents")
    public String getContents(Model model) {
        final List<Content> contents = adminService.getContents();
        final List<ContentModel> contentModels = contents.stream()
                .map(content -> new ContentModel(
                        content.getId(),
                        convertLocalDateTime(content.getMolluDateTime()),
                        convertLocalDateTime(content.getUploadDateTime()),
                        content.getFrontContentSource(),
                        content.getBackContentSource())
                )
                .toList();
        model.addAttribute("contents", contentModels);

        return "contents";
    }

    private String convertLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Operation(summary = "신고된 게시글 조회 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/content-reports")
    public String getContentReports(Model model) {
        final List<ContentReport> contentReports = adminService.getContentReports();
        final Set<Map.Entry<Content, List<ContentReport>>> entries = contentReports.stream()
                .collect(Collectors.groupingBy(ContentReport::getContent))
                .entrySet();

        final List<ContentReportModel> contentReportModels = entries.stream()
                .map(entry -> {
                    final Content content = entry.getKey();
                    final int reportNum = entry.getValue().size();

                    return new ContentReportModel(
                            content.getId(),
                            reportNum,
                            content.getFrontContentSource(),
                            content.getBackContentSource()
                    );
                })
                .toList();

        model.addAttribute("contentReports", contentReportModels);

        return "content-reports";
    }

    @Operation(summary = "신고된 댓글 조회 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/comment-reports")
    public String getCommentReports(Model model) {
        final List<CommentReport> commentReports = adminService.getCommentReports();
        final Set<Map.Entry<Comment, List<CommentReport>>> entries = commentReports.stream()
                .collect(Collectors.groupingBy(CommentReport::getComment))
                .entrySet();

        final List<CommentReportModel> commentReportModels = entries.stream()
                .map(entry -> {
                    final Comment comment = entry.getKey();
                    final int reportNum = entry.getValue().size();

                    return new CommentReportModel(
                            comment.getId(),
                            reportNum,
                            comment.getMessage(),
                            comment.getCreatedAt()
                    );
                })
                .toList();

        model.addAttribute("commentReports", commentReportModels);

        return "comment-reports";
    }
}
