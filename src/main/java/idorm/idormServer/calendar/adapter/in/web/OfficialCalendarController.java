package idorm.idormServer.calendar.adapter.in.web;

import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.CALENDAR_FOUND;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.CALENDAR_MANY_FOUND;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.CALENDAR_UPDATED;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.OFFICIAL_CALENDARS_FOUND;
import static idorm.idormServer.calendar.adapter.out.CalendarResponseCode.OFFICIAL_CALENDAR_DELETED;

import idorm.idormServer.auth.adapter.in.web.Auth;
import idorm.idormServer.auth.adapter.in.web.AuthInfo;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.entity.RoleType;
import idorm.idormServer.calendar.application.port.in.OfficialCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarUpdateRequest;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "X2. Official Calendar", description = "공식 일정 api")
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarController {

  private final OfficialCalendarUseCase officialCalendarUseCase;

  @Auth(role = RoleType.ADMIN)
  @Operation(summary = "[관리자 용] 공식 일정 저장 및 수정", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "CALENDAR_UPDATED",
          content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
      @ApiResponse(responseCode = "400", description = "ILLEGAL_ARGUMENT_DATE_SET"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @PostMapping("/official/calendar")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<SuccessResponse<Object>> update(
      @RequestBody @Valid OfficialCalendarUpdateRequest request
  ) {
    OfficialCalendarResponse response = officialCalendarUseCase.update(request);
    return ResponseEntity.ok().body(SuccessResponse.of(CALENDAR_UPDATED, response));
  }

  @Auth(role = RoleType.ADMIN)
  @Operation(summary = "[관리자 용] 일정 삭제", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "OFFICIAL_CALENDAR_DELETED",
          content = @Content(schema = @Schema(implementation = Object.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @DeleteMapping("/official/calendar/{official-calendar-id}")
  public ResponseEntity<SuccessResponse<Object>> delete(
      @PathVariable(value = "official-calendar-id")
      @Positive(message = "삭제할 공식 일정 식별자는 양수만 가능합니다.") Long officialCalendarId
  ) {
    officialCalendarUseCase.delete(officialCalendarId);
    return ResponseEntity.ok().body(SuccessResponse.from(OFFICIAL_CALENDAR_DELETED));
  }

  @Auth(role = RoleType.ADMIN)
  @Operation(summary = "[관리자 용] 공식 일정 전체 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "OFFICIAL_CALENDARS_FOUND",
          content = @Content(schema = @Schema(implementation = CrawledOfficialCalendarResponse.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/official/calendar")
  public ResponseEntity<SuccessResponse<Object>> findManyByAdmin() {
    List<CrawledOfficialCalendarResponse> responses = officialCalendarUseCase.findByMonthByAdmin();
    return ResponseEntity.ok().body(SuccessResponse.of(OFFICIAL_CALENDARS_FOUND, responses));
  }

  @Auth(role = RoleType.ADMIN)
  @Operation(summary = "[관리자 용] 공식 일정 단건 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "CALENDAR_FOUND",
          content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "404", description = "NOT_FOUND_CALENDAR"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @GetMapping("/official/calendar/{official-calendar-id}")
  public ResponseEntity<SuccessResponse<Object>> findOne(
      @PathVariable(value = "official-calendar-id")
      @Positive(message = "삭제할 일정 식별자는 양수만 가능합니다.") Long officialCalendarId
  ) {
    OfficialCalendarResponse response = officialCalendarUseCase.findOneByAdmin(officialCalendarId);
    return ResponseEntity.ok().body(SuccessResponse.of(CALENDAR_FOUND, response));
  }

  @Auth
  @Operation(summary = "[사용자 용] 공식 일정 월별 조회", security = {
      @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)})
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "CALENDAR_MANY_FOUND",
          content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
      @ApiResponse(responseCode = "400", description = "ILLEGAL_ARGUMENT_DATE_SET"),
      @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_ACCESS_TOKEN"),
      @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
      @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
  })
  @PostMapping("/official/calendar/monthly")
  public ResponseEntity<SuccessResponse<Object>> findManyByMember(
      @AuthInfo AuthResponse authResponse,
      @RequestBody @Valid FindCalendarsRequest request
  ) {
    List<OfficialCalendarResponse> responses = officialCalendarUseCase.findByMonthByMember(
        authResponse,
        request);
    return ResponseEntity.ok().body(SuccessResponse.of(CALENDAR_MANY_FOUND, responses));
  }
}
