package sleepy.mollu.server.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sleepy.mollu.server.admin.controller.model.UserModel;
import sleepy.mollu.server.member.domain.Member;
import sleepy.mollu.server.member.repository.MemberRepository;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

import java.util.List;

@Tag(name = "관리자")
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberRepository memberRepository;

    @Operation(summary = "관리자 로그인 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/dashboard")
    public String getDashboard() {

        return "index";
    }

    @Operation(summary = "사용자 조회 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/users")
    public String getTable(Model model) {
        final List<Member> members = memberRepository.findAll();
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


}
