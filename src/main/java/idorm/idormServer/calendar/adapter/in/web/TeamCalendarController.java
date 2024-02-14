package idorm.idormServer.calendar.adapter.in.web;

import idorm.idormServer.calendar.application.port.in.TeamCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarSaveRequest;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarSummaryResponse;
import idorm.idormServer.calendar.application.port.in.dto.RoomMateCalendarUpdateRequest;
import idorm.idormServer.common.dto.DefaultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
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
public class TeamCalendarController {

    private final TeamCalendarUseCase teamCalendarUseCase;

    @Operation(summary = "[팀] 일정 생성", description = "- targets 필드는 회원 식별자 배열을 주세요")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "TEAM_CALENDER_CREATED",
                    content = @Content(schema = @Schema(implementation = RoomMateCalendarResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "- ILLEGAL_ARGUMENT_DATE_SET\n- *_FIELD_REQUIRED\n- *_LENGTH_INVALID\n" +
                            "- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- TARGETS_FIELD_REQUIRED\n- DATE_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "404",
                    description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND\n- TEAMMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PostMapping("/member/team/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> createTeamCalender(
            HttpServletRequest servletRequest,
            @RequestBody @Valid RoomMateCalendarSaveRequest request
    ) {

        teamCalendarUseCase.save(request);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_CREATED")
                        .responseMessage("팀 일정 생성 완료")
                        .build()
                );
    }

    @Operation(summary = "[팀] 일정 수정", description = "- targets 필드는 회원 식별자 배열을 주세요")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_CALENDER_UPDATED",
                    content = @Content(schema = @Schema(implementation = RoomMateCalendarResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "- *_FIELD_REQUIRED\n- *_LENGTH_INVALID\n- TEAMCALENDARID_NEGATIVEORZERO_INVALID\n" +
                            "- ILLEGAL_ARGUMENT_DATE_SET\n- ILLEGAL_STATEMENT_EXPLODEDTEAM\n- TARGETS_FIELD_REQUIRED\n" +
                            "- ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR\n- DATE_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM_CALENDAR"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMMEMBER_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PutMapping("/member/team/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> updateTeamCalender(
            HttpServletRequest servletRequest,
            @RequestBody @Valid RoomMateCalendarUpdateRequest request
    ) {

        teamCalendarUseCase.update(request);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_UPDATED")
                        .responseMessage("팀 일정 수정 완료")
                        .build()
                );
    }

    @Operation(summary = "[팀] 일정 삭제", description = "- 팀일정은 팀에 소속된 회원이라면 누구나 삭제 가능합니다.\n" +
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
            HttpServletRequest servletRequest,
            @RequestParam(value = "teamCalendarId")
            @Positive(message = "삭제할 팀일정 식별자는 양수만 가능합니다.")
            Long teamCalendarId
    ) {

        teamCalendarUseCase.delete(teamCalendarId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_DELETED")
                        .responseMessage("팀 일정 삭제 완료")
                        .build()
                );
    }

    @Operation(summary = "[팀] 일정 단건 조회")
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
            HttpServletRequest servletRequest,
            @RequestParam(value = "teamCalendarId")
            @Positive(message = "조회할 팀일정 식별자는 양수만 가능합니다.")
            Long teamCalendarId
    ) {

        teamCalendarUseCase.findById(teamCalendarId);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_FOUND")
                        .responseMessage("팀 일정 단건 조회 완료")
                        .build()
                );
    }

    @Operation(summary = "[팀] 일정 월별 조회", description = "- 종료일이 지난 일정도 전부 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_CALENDERS_FOUND",
                    content = @Content(schema = @Schema(implementation = RoomMateCalendarSummaryResponse.class))),
            @ApiResponse(responseCode = "400", description = "YEARMONTH_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "404",
                    description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @PostMapping("/member/team/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findTeamCalenders(
            HttpServletRequest servletRequest,
            @RequestBody @Valid OfficialCalendarsFindRequest request
    ) {

        teamCalendarUseCase.findAllByMonth(request.getYearMonth());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDERS_FOUND")
                        .responseMessage("팀 일정 월별 조회 완료")
                        .build()
                );
    }
}
