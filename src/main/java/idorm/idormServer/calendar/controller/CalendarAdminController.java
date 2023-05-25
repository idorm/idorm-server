package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.dto.*;
import idorm.idormServer.calendar.service.CalendarService;
import idorm.idormServer.common.DefaultResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "[스웨거용] 일정 관리")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarAdminController {

    private final CalendarService calendarService;

    @ApiOperation(value = "[관리자 로그인] 일정 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CALENDAR_SAVED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> save(
            @RequestBody @Valid CalendarSaveRequestDto request
    ) {
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        Calendar calendar = calendarService.save(request.toEntity());
        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_SAVED")
                        .responseMessage("Calendar 일정 저장 완료")
                        .data(response)
                        .build());
    }

    /**
     * 일정 수정
     */
    @ApiOperation(value = "[관리자 로그인] 일정 수정 - 내용")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar/content")
    public ResponseEntity<DefaultResponseDto<Object>> updateContent(
            @RequestBody @Valid CalendarUpdateContentRequestDto request
    ) {

        Calendar calendar = calendarService.findOneById(request.getCalendarId());
        calendarService.updateContent(calendar, request);

        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_UPDATED")
                        .responseMessage("Calendar 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "[관리자 로그인] 일정 수정 - 1,2,3 기숙사 대상 여부")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar/isDormYn")
    public ResponseEntity<DefaultResponseDto<Object>> updateIsDormYn(
            @RequestBody @Valid CalendarUpdateIsDormYnRequestDto request
    ) {

        Calendar calendar = calendarService.findOneById(request.getCalendarId());
        calendarService.updateIsDormYn(calendar, request);

        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_UPDATED")
                        .responseMessage("Calendar 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "[관리자 로그인] 일정 수정 - 위치")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar/location")
    public ResponseEntity<DefaultResponseDto<Object>> updateLocation(
            @RequestBody @Valid CalendarUpdateLocationRequestDto request
    ) {

        Calendar calendar = calendarService.findOneById(request.getCalendarId());
        calendarService.updateLocation(calendar, request);

        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_UPDATED")
                        .responseMessage("Calendar 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "[관리자 로그인] 일정 수정 - 시작 및 종료 일자")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar/date")
    public ResponseEntity<DefaultResponseDto<Object>> updateDate(
            @RequestBody @Valid CalendarUpdateStartAndEndDateRequestDto request
    ) {

        Calendar calendar = calendarService.findOneById(request.getCalendarId());

        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        calendarService.updateDate(calendar, request);

        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_UPDATED")
                        .responseMessage("Calendar 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "[관리자 로그인] 일정 수정 - 시작 및 종료 시간")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar/time")
    public ResponseEntity<DefaultResponseDto<Object>> updateTime(
            @RequestBody @Valid CalendarUpdateStartAndEndTimeRequestDto request
    ) {

        Calendar calendar = calendarService.findOneById(request.getCalendarId());

        calendarService.updateTime(calendar, request);

        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_UPDATED")
                        .responseMessage("Calendar 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @ApiOperation(value = "[관리자 로그인] 일정 수정 - url")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar/url")
    public ResponseEntity<DefaultResponseDto<Object>> updateUrl(
            @RequestBody @Valid CalendarUpdateUrlRequestDto request
    ) {

        Calendar calendar = calendarService.findOneById(request.getCalendarId());
        calendarService.updateUrl(calendar, request);

        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_UPDATED")
                        .responseMessage("Calendar 일정 수정 완료")
                        .data(response)
                        .build());
    }

    /**
     * 일정 수정 end
     */

    @ApiOperation(value = "[관리자 로그인] 일정 삭제", notes = "관리자 계정으로 로그인 후 사용하세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @DeleteMapping("/admin/calendar/{calendar-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> delete(
            @PathVariable(value = "calendar-id")
            @Positive(message = "삭제할 일정 식별자는 양수만 가능합니다.")
            Long calendarId
    ) {

        Calendar calendar = calendarService.findOneById(calendarId);
        calendarService.delete(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_DELETED")
                        .responseMessage("Calendar 일정 삭제 완료")
                        .build());
    }

    @ApiOperation(value = "[비로그인] 일정 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findMany() {

        List<Calendar> calendars = calendarService.findMany();

        List<CalendarDefaultResponseDto> responses = calendars.stream()
                .map(calendar -> new CalendarDefaultResponseDto(calendar)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_MANY_FOUND")
                        .responseMessage("Calendar 일정 전체 조회")
                        .data(responses)
                        .build()
                );
    }

    @ApiOperation(value = "[비로그인] 일정 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_FOUND",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "404",
                    description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/calendar/{calendar-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOne(
            @PathVariable(value = "calendar-id")
            @Positive(message = "삭제할 일정 식별자는 양수만 가능합니다.")
            Long calendarId
    ) {

        Calendar calendar = calendarService.findOneById(calendarId);
        CalendarDefaultResponseDto response = new CalendarDefaultResponseDto(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_FOUND")
                        .responseMessage("Calendar 일정 단건 조회")
                        .data(response)
                        .build()
                );
    }
}
