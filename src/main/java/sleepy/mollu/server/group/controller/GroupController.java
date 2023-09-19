package sleepy.mollu.server.group.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sleepy.mollu.server.group.controller.dto.CreateGroupRequest;
import sleepy.mollu.server.group.controller.dto.CreateGroupResponse;
import sleepy.mollu.server.group.controller.dto.SearchGroupCodeResponse;
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
    public ResponseEntity<GroupMemberSearchResponse> groupSearchFeedResponse(@Login String memberId, @PathVariable String groupId) {

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
    public ResponseEntity<CreateGroupResponse> createGroup(@Login String memberId, @ModelAttribute @Valid CreateGroupRequest request) {

        final CreateGroupResponse response = groupService.createGroup(memberId, request);
        final URI uri = URI.create("/groups/" + response.groupResponse().id());

        return ResponseEntity.created(uri).body(response);
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
}
