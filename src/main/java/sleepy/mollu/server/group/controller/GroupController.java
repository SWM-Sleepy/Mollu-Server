package sleepy.mollu.server.group.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.group.controller.dto.CreateGroupRequest;
import sleepy.mollu.server.group.controller.dto.JoinGroupByCodeRequest;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupResponse;
import sleepy.mollu.server.group.dto.GroupMemberSearchResponse;
import sleepy.mollu.server.group.dto.MyGroupResponse;
import sleepy.mollu.server.group.service.GroupService;
import sleepy.mollu.server.oauth2.controller.annotation.Login;
import sleepy.mollu.server.swagger.*;

import java.net.URI;

@Tag(name = "그룹 관련 API")
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 피드 검색")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @ForbiddenResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/{groupId}/members")
    public ResponseEntity<GroupMemberSearchResponse> searchGroupMembers(@Login String memberId, @PathVariable String groupId) {

        return ResponseEntity.ok(groupService.searchGroupMembers(memberId, groupId));
    }

    @Operation(summary = "소속 그룹 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/my")
    public ResponseEntity<MyGroupResponse> searchMyGroups(@Login String memberId) {

        return ResponseEntity.ok(groupService.searchMyGroups(memberId));
    }

    @Operation(summary = "그룹 생성")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping
    public ResponseEntity<Void> createGroup(@Login String memberId, @ModelAttribute @Valid CreateGroupRequest request) {

        final String groupId = groupService.createGroup(memberId, request);
        final URI uri = URI.create("/groups/" + groupId);

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "그룹 초대 코드 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/{groupId}/code")
    public ResponseEntity<SearchGroupCodeResponse> searchGroupCode(@Login String memberId, @PathVariable String groupId) {

        return ResponseEntity.ok(groupService.searchGroupCode(memberId, groupId));
    }

    @Operation(summary = "초대 코드로 그룹 조회")
    @OkResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @GetMapping("/code")
    public ResponseEntity<SearchGroupResponse> searchGroupByCode(@Login String memberId, @RequestParam String code) {

        return ResponseEntity.ok(groupService.searchGroupByCode(memberId, code));
    }

    @Operation(summary = "초대 코드로 그룹 참여")
    @CreatedResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @PostMapping("/code")
    public ResponseEntity<Void> joinGroupByCode(@Login String memberId,
                                                @RequestBody @Valid JoinGroupByCodeRequest request) {

        groupService.joinGroupByCode(memberId, request.code());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "그룹 탈퇴")
    @NoContentResponse
    @BadRequestResponse
    @UnAuthorizedResponse
    @NotFoundResponse
    @InternalServerErrorResponse
    @DeleteMapping("/{groupId}/members")
    public ResponseEntity<Void> leaveGroup(@Login String memberId, @PathVariable String groupId) {

        groupService.leaveGroup(memberId, groupId);
        return ResponseEntity.noContent().build();
    }
}
