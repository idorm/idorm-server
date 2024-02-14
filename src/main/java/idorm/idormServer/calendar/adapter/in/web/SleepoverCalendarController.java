package idorm.idormServer.calendar.adapter.in.web;

import idorm.idormServer.calendar.application.port.in.SleepoverCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarUpdateRequest;
import idorm.idormServer.calendar.application.port.in.dto.SleepoverCalendarsFindRequest;
import idorm.idormServer.common.dto.DefaultResponseDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.support.token.Login;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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

    @Operation(summary = "[외박] 일정 생성", description = "- 외박일정은 본인의 것만 생성 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "SLEEPOVER_CALENDAR_CREATED",
                    content = @Content(schema = @Schema(implementation = RoomMateCalendarResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- ILLEGAL_ARGUMENT_DATE_SET\n" +
                            "-  DATE_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "404", description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "409", description = "DUPLICATE_SLEEPOVER_DATE"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PostMapping("/member/team/calendar/sleepover")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> createSleepoverCalender(
            @Login Member member,
            @RequestBody @Valid SleepoverCalendarRequest request
    ) {

        sleepoverCalendarUseCase.save(member, request);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("SLEEPOVER_CALENDAR_CREATED")
                        .responseMessage("외박 일정 생성 완료")
                        .build()
                );
    }

    @Operation(summary = "[외박] 일정 수정", description = "- 본인의 외박일정만 수정 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "SLEEPOVER_CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = RoomMateCalendarResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- TEAMCALENDARID_FIELD_REQUIRED\n" +
                            "- TEAMCALENDARID_NEGATIVEORZERO_INVALID\n- ILLEGAL_ARGUMENT_DATE_SET\n" +
                            "- DATE_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_SLEEPOVER_CALENDAR"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "409", description = "DUPLICATE_SLEEPOVER_DATE"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PutMapping("/member/team/calendar/sleepover")
    public ResponseEntity<DefaultResponseDto<Object>> updateSleepoverCalender(
            @Login Member member,
            @RequestBody @Valid SleepoverCalendarUpdateRequest request
    ) {

        sleepoverCalendarUseCase.update(member, request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("SLEEPOVER_CALENDAR_UPDATED")
                        .responseMessage("외박 일정 수정 완료")
                        .build()
                );
    }

    @Operation(summary = "[외박] 일정 삭제", description = "- 팀일정은 팀에 소속된 회원이라면 누구나 삭제 가능합니다.\n" +
            "- 외박 일정은 본인의 것만 삭제 가능합니다.")
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
    @DeleteMapping("/member/team/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> deleteTeamCalender(
            @Login Member member,
            @RequestParam(value = "teamCalendarId")
            @Positive(message = "삭제할 팀일정 식별자는 양수만 가능합니다.")
            Long sleepoverCalendarId
    ) {

        sleepoverCalendarUseCase.delete(member, sleepoverCalendarId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_DELETED")
                        .responseMessage("외 일정 삭제 완료")
                        .build()
                );
    }

    @Operation(summary = "[외박] 일정 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_CALENDER_FOUND",
                    content = @Content(schema = @Schema(implementation = RoomMateCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "TEAMCALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM_CALENDAR"),
            @ApiResponse(responseCode = "404",
                    description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND\n- TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/member/team/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> findTeamCalender(
            @Login Member member,
            @RequestParam(value = "teamCalendarId")
            @Positive(message = "조회할 팀일정 식별자는 양수만 가능합니다.")
            Long sleepoverCalendarId
    ) {

        sleepoverCalendarUseCase.findById(member, sleepoverCalendarId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_FOUND")
                        .responseMessage("팀 일정 단건 조회 완료")
                        .build()
                );
    }

    @Operation(summary = "[외박] 일정 월별 조회", description = "- 팀원 한 명의 외박 일정들을 응답합니다.\n" +
            "- 종료일이 지난 일정은 빼고 응답합니다.\n" +
            "- 예시 성공 응답이 리스트로 파바박- 담겨서 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_SLEEPOVER_CALENDERS_FOUND",
                    content = @Content(schema = @Schema(implementation = SleepoverCalendarResponse.class))),
            @ApiResponse(responseCode = "400", description = "YEARMONTH_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM"),
            @ApiResponse(responseCode = "404",
                    description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PostMapping("/member/team/calendars/sleepover")
    public ResponseEntity<DefaultResponseDto<Object>> findTeamSleepoverCalenders(
            @Login Member member,
            @RequestBody @Valid SleepoverCalendarsFindRequest request
    ) {

        sleepoverCalendarUseCase.findAllByMonth(member, request.getYearMonth());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_SLEEPOVER_CALENDERS_FOUND")
                        .responseMessage("팀 외박 일정 월별 조회 완료")
                        .build()
                );
    }
}
