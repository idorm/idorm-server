package idorm.idormServer.calendar.controller;

import idorm.idormServer.calendar.domain.OfficialCalendar;
import idorm.idormServer.calendar.dto.CrawledOfficialCalendarResponse;
import idorm.idormServer.calendar.dto.OfficialCalendarResponse;
import idorm.idormServer.calendar.dto.OfficialCalendarUpdateRequest;
import idorm.idormServer.calendar.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.calendar.service.OfficialCalendarService;
import idorm.idormServer.common.dto.DefaultResponseDto;
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
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.config.SecurityConfig.API_ROOT_URL_V1;

@Tag(name = "2. Official Calendar", description = "공식 일정 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficialCalendarController {

    private final OfficialCalendarService calendarService;

    @Operation(summary = "[관리자 용] 공식 일정 저장 및 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "ILLEGAL_ARGUMENT_DATE_SET"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PostMapping("/admin/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> update(
            @RequestBody @Valid OfficialCalendarUpdateRequest request
    ) {
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        OfficialCalendar calendar = calendarService.findOneById(request.getCalendarId());
        calendarService.update(calendar, request);

        OfficialCalendarResponse response = new OfficialCalendarResponse(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_UPDATED")
                        .responseMessage("공식 일정 수정 완료")
                        .data(response)
                        .build());
    }

    @Operation(summary = "[관리자 용] 일정 삭제")
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
            @Positive(message = "삭제할 공식 일정 식별자는 양수만 가능합니다.") Long officialCalendarId
    ) {

        OfficialCalendar calendar = calendarService.findOneById(officialCalendarId);
        calendarService.delete(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_DELETED")
                        .responseMessage("공식 일정 삭제 완료")
                        .build());
    }

    @Operation(summary = "[관리자 용] 공식 일정 전체 조회", description = "- 리스트에 일정이 담겨서 응답합니다.\n" +
            "- 일정 조회 조건은 다음과 같습니다.\n" +
            "\t- 생활원 게시글 작성일자 : <b>이번 달</b> 혹은 <b>일주일 전의 달</b> 인 경우\n" +
            "- 응답 필드의 <b>isPublic(사용자 공개 여부, 관리자 허가 여부)</b> 값으로 프론트에서 추가 가공이 필요합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "OFFICIAL_CALENDARS_FOUND",
                    content = @Content(schema = @Schema(implementation = CrawledOfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED_MEMBER"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/admin/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findManyByAdmin() {

        List<OfficialCalendar> calendars = calendarService.findManyByAdmin();
        Collections.reverse(calendars);

        List<CrawledOfficialCalendarResponse> responses = calendars.stream()
                .map(CrawledOfficialCalendarResponse::new).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDARS_FOUND")
                        .responseMessage("관리자용 월별 공식 일정 조회 완료")
                        .data(responses)
                        .build());
    }

    @Operation(summary = "[관리자 용] 공식 일정 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "CALENDAR_FOUND",
                    content = @Content(schema = @Schema(implementation = OfficialCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "CALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "404", description = "CALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/admin/calendar/{official-calendar-id}")
    public ResponseEntity<DefaultResponseDto<Object>> findOne(
            @PathVariable(value = "official-calendar-id")
            @Positive(message = "삭제할 일정 식별자는 양수만 가능합니다.") Long calendarId
    ) {

        OfficialCalendar calendar = calendarService.findOneById(calendarId);
        OfficialCalendarResponse response = new OfficialCalendarResponse(calendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDAR_FOUND")
                        .responseMessage("관리자용 공식 일정 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @Operation(summary = "[사용자 용] 공식 일정 월별 조회", description = "- 모든 기숙사의 일정을 반환합니다. \n" +
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
    public ResponseEntity<DefaultResponseDto<Object>> findManyByMember(
            @RequestBody @Valid OfficialCalendarsFindRequest request
    ) {

        List<OfficialCalendar> calendars = calendarService.findManyByYearMonth(request.getYearMonth());

        calendars.removeIf(calendar -> calendar.getEndDate().isBefore(LocalDateTime.now().plusHours(9).toLocalDate()));
        calendars.sort(Comparator.comparing(OfficialCalendar::getId, Comparator.reverseOrder()));

        List<OfficialCalendarResponse> responses = calendars.stream()
                .map(OfficialCalendarResponse::new).collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("OFFICIAL_CALENDARS_FOUND")
                        .responseMessage("월별 공식 일정 다건 조회")
                        .data(responses)
                        .build()
                );
    }
}
