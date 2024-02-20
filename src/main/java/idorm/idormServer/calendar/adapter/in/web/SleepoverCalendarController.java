package idorm.idormServer.calendar.adapter.in.web;

import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.SLEEPOVER_CALENDAR_CREATED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_DELETED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_FOUND;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_CALENDER_UPDATED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.TEAM_SLEEPOVER_CALENDERS_FOUND;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.domain.Auth;
import idorm.idormServer.auth.domain.AuthInfo;
import idorm.idormServer.calendar.application.port.in.SleepoverCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.CalendarsResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindSleepoverCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveSleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateSleepoverCalendarRequest;
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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "4. RoomMate TeamJpaEntity Calendar", description = "룸메이트 팀 일정 api")
@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SleepoverCalendarController {

  private final SleepoverCalendarUseCase sleepoverCalendarUseCase;

  @Auth
  @Operation(summary = "[외박] 일정 생성", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "SLEEPOVER_CALENDAR_CREATED",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400",
          description = "- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- ILLEGAL_ARGUMENT_DATE_SET\n" +
              "-  DATE_FIELD_REQUIRED"),
      @ApiResponse(responseCode = "404", description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND"),
      @ApiResponse(responseCode = "409", description = "DUPLICATE_SLEEPOVER_DATE"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @PostMapping("/calendar/sleepover")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<SuccessResponse<Object>> createSleepoverCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid SaveSleepoverCalendarRequest request
  ) {
    SleepoverCalendarResponse response = sleepoverCalendarUseCase.save(authResponse, request);
    return ResponseEntity.ok().body(SuccessResponse.of(SLEEPOVER_CALENDAR_CREATED, response));
  }

  @Auth
  @Operation(summary = "[외박] 일정 수정", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "SLEEPOVER_CALENDAR_UPDATED",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400",
          description = "- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- TEAMCALENDARID_FIELD_REQUIRED\n" +
              "- TEAMCALENDARID_NEGATIVEORZERO_INVALID\n- ILLEGAL_ARGUMENT_DATE_SET\n" +
              "- DATE_FIELD_REQUIRED"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_SLEEPOVER_CALENDAR"),
      @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
      @ApiResponse(responseCode = "409", description = "DUPLICATE_SLEEPOVER_DATE"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
  })
  @PutMapping("/calendar/sleepover")
  public ResponseEntity<SuccessResponse<Object>> updateSleepoverCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid UpdateSleepoverCalendarRequest request
  ) {
    SleepoverCalendarResponse response = sleepoverCalendarUseCase.update(authResponse, request);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_CALENDER_UPDATED, response));
  }

  @Auth
  @Operation(summary = "[외박] 일정 삭제", security = {
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
          description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND\n- TEAMCALENDAR_NOT_FOUND"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @DeleteMapping("/calendar/sleepover")
  public ResponseEntity<SuccessResponse<Object>> deleteTeamCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestParam(value = "teamCalendarId")
      @Positive(message = "삭제할 팀일정 식별자는 양수만 가능합니다.")
      Long sleepoverCalendarId
  ) {
    sleepoverCalendarUseCase.delete(authResponse, sleepoverCalendarId);
    return ResponseEntity.ok().body(SuccessResponse.from(TEAM_CALENDER_DELETED));
  }

  @Auth
  @Operation(summary = "[외박] 일정 단건 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_CALENDER_FOUND",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400", description = "TEAMCALENDARID_NEGATIVEORZERO_INVALID"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM_CALENDAR"),
      @ApiResponse(responseCode = "404",
          description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND\n- TEAMCALENDAR_NOT_FOUND"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/calendar/sleepover")
  public ResponseEntity<SuccessResponse<Object>> findTeamCalender(
      @AuthInfo AuthResponse authResponse,
      @RequestParam(value = "teamCalendarId")
      @Positive(message = "조회할 팀일정 식별자는 양수만 가능합니다.")
      Long sleepoverCalendarId
  ) {
    SleepoverCalendarResponse response = sleepoverCalendarUseCase.findById(authResponse,
        sleepoverCalendarId);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_CALENDER_FOUND, response));
  }

  @Auth
  @Operation(summary = "[외박] 일정 월별 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "TEAM_SLEEPOVER_CALENDERS_FOUND",
          content = @Content(schema = @Schema(implementation = CalendarsResponse.class))),
      @ApiResponse(responseCode = "400", description = "YEARMONTH_FIELD_REQUIRED"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM"),
      @ApiResponse(responseCode = "404",
          description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @PostMapping("/calendar/sleepover/monthly")
  public ResponseEntity<SuccessResponse<Object>> findTeamSleepoverCalenders(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid FindSleepoverCalendarsRequest request
  ) {
    List<SleepoverCalendarResponse> responses = sleepoverCalendarUseCase.findSleepoverCalendarsByMonth(
        authResponse, request);
    return ResponseEntity.ok().body(SuccessResponse.of(TEAM_SLEEPOVER_CALENDERS_FOUND, responses));
  }
}
