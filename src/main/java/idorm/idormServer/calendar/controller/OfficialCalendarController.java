package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.calendar.service.OfficialCalendarService;
import idorm.idormServer.common.DefaultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;

@Tag(name = "Official Calendar", description = "공식 일정 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarController {

    private final OfficialCalendarService calendarService;

    @Operation(summary = "공식 일정 월별 조회", description = "- 모든 기숙사의 일정을 반환합니다. \n" +
            "- 종료된 일정은 제거한 후, 최신 등록 순으로 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "ILLEGAL_ARGUMENT_DATE_SET"),
            @ApiResponse(responseCode = "401",
                    description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403",
                    description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/member/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findMany(
            @RequestBody @Valid OfficialCalendarsFindRequest request
    ) {

        List<OfficialCalendar> calendars = calendarService.findManyByYearMonth(request.getYearMonth());

        calendars.removeIf(calendar -> calendar.getEndDate().isBefore(LocalDateTime.now().plusHours(9).toLocalDate()));
        calendars.sort(Comparator.comparing(OfficialCalendar::getId, Comparator.reverseOrder()));

        List<OfficialCalendarResponse> responses = calendars.stream()
                .map(calendar -> new OfficialCalendarResponse(calendar)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_MANY_FOUND")
                        .responseMessage("월별 공식 일정 다건 조회")
                        .data(responses)
                        .build()
                );
    }
}
