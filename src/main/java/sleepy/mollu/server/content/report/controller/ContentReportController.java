package sleepy.mollu.server.content.report.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.content.report.dto.ReportRequest;
import sleepy.mollu.server.content.report.service.ReportService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

import java.net.URI;

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

        final String reason = getReason(request);
        final Long reportId = reportService.reportContent(memberId, contentId, reason);
        final URI uri = URI.create("/contents/" + contentId + "/report/" + reportId);

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "댓글 신고")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping("/{contentId}/comments/{commentId}/report")
    public ResponseEntity<Void> reportComment(@Login String memberId,
                                              @PathVariable String contentId,
                                              @PathVariable String commentId,
                                              @RequestBody(required = false) ReportRequest request) {

        final String reason = getReason(request);
        final Long reportId = reportService.reportComment(memberId, contentId, commentId, reason);
        final URI uri = URI.create("/contents/" + contentId + "/comments/" + commentId + "/report/" + reportId);

        return ResponseEntity.created(uri).build();
    }

    private String getReason(ReportRequest request) {
        String reason = "";
        if (request != null) {
            reason = request.reason();
        }
        return reason;
    }
}
