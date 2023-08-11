package sleepy.mollu.server.alarm.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sleepy.mollu.server.alarm.admin.controller.dto.AdminRequest;
import sleepy.mollu.server.alarm.admin.service.AdminService;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

import java.security.NoSuchAlgorithmException;

@Tag(name = "관리자")
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "관리자 로그인 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/login")
    public String getLoginPage() {

        return "login";
    }

    @Operation(summary = "관리자 로그인")
    @OkResponse
    @InternalServerErrorResponse
    @PostMapping("/login")
    public String login(@ModelAttribute AdminRequest request, HttpSession session) throws NoSuchAlgorithmException {

        if (adminService.isAdmin(request.id(), request.password())) {
            session.setAttribute("adminSession", request.id());
            return "redirect:/admin/alarm/mollu-range";
        }

        return "redirect:/admin/login";
    }
}
