package idorm.idormServer.calendar.adapter.in.web;

import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_CREATED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_DELETED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_FOUND;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_UPDATED;

import idorm.idormServer.auth.adapter.in.api.Auth;
import idorm.idormServer.auth.adapter.in.api.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.TeamCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.CalendarsResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindOfficialCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveTeamCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.TeamCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateTeamCalendarRequest;
import idorm.idormServer.common.response.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "X. Team Calendar", description = "룸메이트 팀 일정 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class TeamCalendarController {

  private final TeamCalendarUseCase teamCalendarUseCase;

  @Auth
  @Operation(summary = "[팀] 일정 생성", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "TEAM_CALENDER_CREATED",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400",
          description = "- ILLEGAL_ARGUMENT_DATE_SET\n- *_FIELD_REQUIRED\n- *_LENGTH_INVALID\n" +
              "- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- TARGETS_FIELD_REQUIRED\n- DATE_FIELD_REQUIRED"),
      @ApiResponse(responseCode = "404",
          description = "- NOT_FOUND_MEMBER\n- NOT_FOUND_TEAM\n- NOT_FOUND_TEAM_MEMBER"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @PostMapping("/calendar/team")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<SuccessResponse<Object>> createTeamCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid SaveTeamCalendarRequest request
  ) {
    TeamCalendarResponse response = teamCalendarUseCase.save(authResponse, request);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_CALENDER_CREATED, response));
  }

  @Auth
  @Operation(summary = "[팀] 일정 수정", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_CALENDER_UPDATED",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400",
          description =
              "- *_FIELD_REQUIRED\n- *_LENGTH_INVALID\n- TEAMCALENDARID_NEGATIVEORZERO_INVALID\n" +
                  "- ILLEGAL_ARGUMENT_DATE_SET\n- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- TARGETS_FIELD_REQUIRED\n"
                  +
                  "- ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR\n- DATE_FIELD_REQUIRED"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM_CALENDAR"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_MEMBER / NOT_FOUND_TEAM / NOT_FOUND_TEAM_MEMBER / NOT_FOUND_TEAM_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @PutMapping("/calendar/team")
  public ResponseEntity<SuccessResponse<Object>> updateTeamCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid UpdateTeamCalendarRequest request
  ) {

    TeamCalendarResponse response = teamCalendarUseCase.update(authResponse, request);

    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_CALENDER_UPDATED, response));
  }

  @Auth
  @Operation(summary = "[팀] 일정 삭제", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_CALENDER_DELETED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "400",
          description = "- TEAMCALENDARID_NEGATIVEORZERO_INVALID\n- ILLEGAL_STATEMENT_EXPLODEDTEAM"),
      @ApiResponse(responseCode = "403",
          description = "- ACCESS_DENIED_TEAM_CALENDAR\n- ACCESS_DENIED_SLEEPOVER_CALENDAR"),
      @ApiResponse(responseCode = "404",
          description = "- NOT_FOUND_MEMBER\n- NOT_FOUND_TEAM\n- NOT_FOUND_TEAM_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @DeleteMapping("/calendar/team")
  public ResponseEntity<SuccessResponse<Object>> deleteTeamCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestParam(value = "teamCalendarId")
      @Positive(message = "삭제할 팀일정 식별자는 양수만 가능합니다.")
      Long teamCalendarId
  ) {
    teamCalendarUseCase.delete(authResponse, teamCalendarId);
    return ResponseEntity.ok().body(SuccessResponse.from(TEAM_CALENDER_DELETED));
  }

  @Auth
  @Operation(summary = "[팀] 일정 단건 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_CALENDER_FOUND",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400", description = "TEAMCALENDARID_NEGATIVEORZERO_INVALID"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM_CALENDAR"),
      @ApiResponse(responseCode = "404",
          description = "- NOT_FOUND_MEMBER\n- NOT_FOUND_TEAM\n- NOT_FOUND_TEAM_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/calendar/team")
  public ResponseEntity<SuccessResponse<Object>> findTeamCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestParam(value = "teamCalendarId")
      @Positive(message = "조회할 팀일정 식별자는 양수만 가능합니다.")
      Long teamCalendarId
  ) {
    TeamCalendarResponse response = teamCalendarUseCase.findById(authResponse, teamCalendarId);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_CALENDER_FOUND, response));
  }

  @Auth
  @Operation(summary = "[팀] 일정 월별 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_CALENDERS_FOUND",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400", description = "YEARMONTH_FIELD_REQUIRED"),
      @ApiResponse(responseCode = "404",
          description = "- NOT_FOUND_MEMBER\n- NOT_FOUND_TEAM"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @PostMapping("/calendar/team/monthly")
  public ResponseEntity<SuccessResponse<Object>> findTeamCalenders(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid FindOfficialCalendarsRequest request
  ) {
    List<TeamCalendarResponse> responses = teamCalendarUseCase.findTeamCalendarsByMonth(
        authResponse, request);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_CALENDER_FOUND, responses));
  }
}
