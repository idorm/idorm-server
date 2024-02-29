package idorm.idormServer.calendar.adapter.in.web;

import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_EXPLODED_CHECKED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_MEMBERS_FOUND;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_MEMBER_CREATED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_MEMBER_DELETED;

import idorm.idormServer.auth.adapter.in.api.Auth;
import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.TeamUseCase;
import idorm.idormServer.calendar.application.port.in.dto.TeamResponse;
import idorm.idormServer.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "9. Team", description = "룸메이트 팀원 관리 api")
@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamController {

  private final TeamUseCase teamUseCase;

  @Auth
  @Operation(summary = "룸메이트 초대 수락", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "TEAM_MEMBER_CREATED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "400",
          description =
              "- REGISTERMEMBERID_NEGATIVEORZERO_INVALID\n- ILLEGAL_STATEMENT_EXPLODEDTEAM\n" +
                  "- ACCESS_DENIED_ADMIN"),
      @ApiResponse(responseCode = "404",
          description = "- NOT_FOUND_MEMBER\n- NOT_FOUND_TEAM"),
      @ApiResponse(responseCode = "409",
          description = "- ALREADY_REGISTERED_TEAM\n- CANNOT_REGISTER_TEAM_STATUS_FULL"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @PostMapping("/member/team")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<SuccessResponse<Object>> addTeamMember(
      @AuthInfo AuthResponse authResponse,
      @RequestParam(value = "registerMemberId")
      @Positive(message = "회원 식별자는 양수만 가능합니다.")
      Long registerMemberId
  ) {
    teamUseCase.addTeamMember(authResponse, registerMemberId);
    return ResponseEntity.ok().body(SuccessResponse.from(TEAM_MEMBER_CREATED));
  }

  @Auth
  @Operation(summary = "팀원 삭제", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_MEMBER_DELETED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "400", description = "MEMBERID_NEGATIVEORZERO_INVALID"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_TEAM"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @DeleteMapping("/member/team")
  public ResponseEntity<SuccessResponse<Object>> deleteTeamMember(
      @AuthInfo AuthResponse authResponse,
      @RequestParam(value = "memberId")
      @Positive(message = "삭제할 회원 식별자는 양수만 가능합니다.")
      Long memberId
  ) {
    teamUseCase.deleteMember(authResponse, memberId);
    return ResponseEntity.ok().body(SuccessResponse.from(TEAM_MEMBER_DELETED));
  }

  @Auth
  @Operation(summary = "팀원 전체 조회", security = {@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_MEMBERS_FOUND",
          content = @Content(schema = @Schema(implementation = TeamResponse.class))),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/member/team")
  public ResponseEntity<SuccessResponse<Object>> findTeamMembers(
      @AuthInfo AuthResponse authResponse
  ) {
    TeamResponse responses = teamUseCase.findTeam(authResponse);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_MEMBERS_FOUND, responses));
  }

  @Auth
  @Operation(summary = "팀 폭발 확인 OK", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_EXPLODED_CHECKED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "409", description = "CANNOT_EXPLODE_TEAM"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @PatchMapping("/member/team")
  public ResponseEntity<SuccessResponse<Object>> isConfirmTeamExploded(
      @AuthInfo AuthResponse authResponse
  ) {
    teamUseCase.explodeTeam(authResponse);
    return ResponseEntity.ok().body(SuccessResponse.from(TEAM_EXPLODED_CHECKED));
  }
}
