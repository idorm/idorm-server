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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "일정")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CalendarController {

    private final CalendarService calendarService;

    @ApiOperation(value = "일정 다건 조회", notes = "- 모든 기숙사의 일정을 반환합니다. \n" +
            "- 서버에서 종료된 일정은 제거 및 일정의 시작일자 순으로 정렬하여 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = CalendarDefaultResponseDto.class))),
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

        calendars.removeIf(calendar -> calendar.getEndDate().isBefore(LocalDateTime.now().plusHours(9).toLocalDate()));
        calendars.sort(Comparator.comparing(Calendar::getStartDate));

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
