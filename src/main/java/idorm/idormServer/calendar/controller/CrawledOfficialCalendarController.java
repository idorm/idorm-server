package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.dto.OfficialCalendarSaveRequest;
import idorm.idormServer.calendar.dto.OfficialCalendarUpdateRequest;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;

//@Hidden
@Tag(name = "ADMIN", description = "공식 일정 관리 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawledOfficialCalendarController {

    private final OfficialCalendarService calendarService;

    // TODO: 크롤링한 새로운 게시글 조회 API 구현

    @Operation(summary = "[관리자 로그인] 일정 저장")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "CALENDAR_SAVED",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "ILLEGAL_ARGUMENT_DATE_SET"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> save(
            @RequestBody @Valid OfficialCalendarSaveRequest request
    ) {
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        OfficialCalendar calendar = calendarService.save(request.toEntity());
        OfficialCalendarResponse response = new OfficialCalendarResponse(calendar);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_SAVED")
                        .responseMessage("공식 일정 저장 완료")
                        .data(response)
                        .build());
    }

    @Operation(summary = "[관리자 로그인] 일정 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "ILLEGAL_ARGUMENT_DATE_SET"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PutMapping("/admin/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> updaate(
            @RequestBody @Valid OfficialCalendarUpdateRequest request
    ) {

        OfficialCalendar officialCalendar = calendarService.findOneById(request.getCalendarId());
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        calendarService.update(officialCalendar, request);
        OfficialCalendarResponse response = new OfficialCalendarResponse(officialCalendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_UPDATED")
                        .responseMessage("공식 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @Operation(summary = "[비로그인] 일정 전체 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "CALENDAR_MANY_FOUND",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findMany() {

        List<OfficialCalendar> calendars = calendarService.findMany();

        List<OfficialCalendarResponse> responses = calendars.stream()
                .map(calendar -> new OfficialCalendarResponse(calendar)).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_MANY_FOUND")
                        .responseMessage("Calendar 일정 전체 조회")
                        .data(responses)
                        .build()
                );
    }

    @Operation(summary = "[관리자 로그인] 일정 삭제", description = "관리자 계정으로 로그인 후 사용하세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OFFICIAL_CALENDAR_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @DeleteMapping("/admin/calendar/{official-calendar-id}")
    public ResponseEntity<DefaultResponseDto<Object>> delete(
            @PathVariable(value = "official-calendar-id")
            @Positive(message = "삭제할 공식 일정 식별자는 양수만 가능합니다.")
            Long officialCalendarId
    ) {

        OfficialCalendar calendar = calendarService.findOneById(officialCalendarId);
        calendarService.delete(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_DELETED")
                        .responseMessage("공식 일정 삭제 완료")
                        .build());
    }

    @Operation(summary = "[비로그인] 일정 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "CALENDAR_FOUND",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/calendar/{calendar-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOne(
            @PathVariable(value = "calendar-id")
            @Positive(message = "삭제할 일정 식별자는 양수만 가능합니다.")
            Long calendarId
    ) {

        OfficialCalendar calendar = calendarService.findOneById(calendarId);
        OfficialCalendarResponse response = new OfficialCalendarResponse(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("CALENDAR_FOUND")
                        .responseMessage("Calendar 일정 단건 조회")
                        .data(response)
                        .build()
                );
    }
}
