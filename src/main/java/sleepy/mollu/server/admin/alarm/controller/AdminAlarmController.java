package sleepy.mollu.server.admin.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sleepy.mollu.server.admin.alarm.dto.MolluRangeRequest;
import sleepy.mollu.server.admin.alarm.dto.MolluRangeResponse;
import sleepy.mollu.server.admin.alarm.dto.MolluTimeResponse;
import sleepy.mollu.server.admin.alarm.service.AdminAlarmService;
import sleepy.mollu.server.swagger.InternalServerErrorResponse;
import sleepy.mollu.server.swagger.OkResponse;

import java.util.List;

@Tag(name = "알림 어드민 페이지")
@Controller
@RequestMapping("/admin/alarm")
@RequiredArgsConstructor
public class AdminAlarmController {

    private final AdminAlarmService adminAlarmService;

    @Operation(summary = "mollu 타임 시간 범위 조회")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/mollu-range")
    public String searchMolluAlarmRange(Model model) {

        final MolluRangeResponse molluRange = adminAlarmService.searchMolluAlarmRange();
        model.addAttribute("molluRange", molluRange);

        return "mollu-range";
    }

    @Operation(summary = "mollu 타임 시간 범위 변경")
    @OkResponse
    @InternalServerErrorResponse
    @PostMapping("/mollu-range")
    public String updateMolluAlarmRange(@ModelAttribute MolluRangeRequest request) {

        adminAlarmService.updateMolluAlarmRange(request.from(), request.to());

        return "redirect:mollu-range";
    }

    @Operation(summary = "예약된 mollu 타임 조회")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/mollu-time")
    public String searchMolluTime(Model model) {

        final List<MolluTimeResponse> molluTimes = adminAlarmService.searchMolluTimes();
        model.addAttribute("molluTimes", molluTimes);

        return "mollu-time";
    }

    @Operation(summary = "알림 수동 전송 페이지")
    @OkResponse
    @InternalServerErrorResponse
    @GetMapping("/mollu-alarm")
    public String molluAlarmPage() {

        return "mollu-alarm";
    }

    @Operation(summary = "알림 수동 전송")
    @OkResponse
    @InternalServerErrorResponse
    @PostMapping("/mollu-alarm")
    public String sendAlarm() {

        adminAlarmService.sendMolluAlarm();

        return "redirect:mollu-alarm";
    }
}
