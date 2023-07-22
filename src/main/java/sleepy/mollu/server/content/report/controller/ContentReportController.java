package sleepy.mollu.server.content.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.content.report.dto.ReportRequest;
import sleepy.mollu.server.content.report.service.ReportService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

@RestController
@Tag(name = "컨텐츠 신고 관련 API")
@RequestMapping("/contents")
@RequiredArgsConstructor
public class ContentReportController {

    private final ReportService reportService;

    @Operation(summary = "컨텐츠 신고")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping("/{contentId}/report")
    public ResponseEntity<Void> groupSearchFeedResponse(@Login String memberId, @PathVariable String contentId,
                                                        @RequestBody(required = false) ReportRequest request) {

        reportService.reportContent(memberId, contentId, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
