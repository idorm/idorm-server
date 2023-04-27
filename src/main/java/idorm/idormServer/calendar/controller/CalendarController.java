package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.domain.Calendar;
import idorm.idormServer.calendar.dto.CalendarDefaultResponseDto;
import idorm.idormServer.calendar.dto.CalendarFindManyRequestDto;
import idorm.idormServer.calendar.dto.CalendarSaveRequestDto;
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

@Api(tags = "일정")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarController {

    private final CalendarService calendarService;

    @ApiOperation(value = "[스웨거용] 일정 저장", notes = "관리자 계정으로 로그인 후 사용하세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "CALENDAR_SAVED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
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

        calendarService.save(request.toEntity());

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_SAVED")
                        .responseMessage("Calendar 일정 저장 완료")
                        .build());
    }

    @ApiOperation(value = "[스웨거용] 일정 삭제", notes = "관리자 계정으로 로그인 후 사용하세요.")
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

    @ApiOperation(value = "[스웨거용] 일정 단건 조회", notes = "- 로그인하지 않아도 확인 가능합니다.")
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

    @ApiOperation(value = "일정 다건 조회", notes = "- 모든 기숙사의 일정을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "DATE_SET_INVALID"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_AUTHORIZATION"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/member/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findMany(
            @RequestBody @Valid CalendarFindManyRequestDto request
    ) {

        List<Calendar> calendars = calendarService.findManyByYearMonth(request.getYearMonth());

        List<CalendarDefaultResponseDto> responses = calendars.stream()
                .map(calendar -> new CalendarDefaultResponseDto(calendar)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_MANY_FOUND")
                        .responseMessage("Calendar 월별 일정 다건 조회")
                        .data(responses)
                        .build()
                );
    }
}
