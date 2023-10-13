package sleepy.mollu.server.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

@Tag(name = "관리자")
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Operation(summary = "관리자 로그인 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/dashboard")
    public String getDashboard() {

        return "index";
    }
}
