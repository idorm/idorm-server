package idorm.idormServer.calendar.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.calendar.dto.Calendar.CalendarFindManyRequestDto;
import idorm.idormServer.calendar.dto.Team.TeamMemberFindResponseDto;
import idorm.idormServer.calendar.dto.TeamCalendar.*;
import idorm.idormServer.calendar.service.CalendarService;
import idorm.idormServer.calendar.service.TeamCalendarService;
import idorm.idormServer.calendar.service.TeamService;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.exception.ExceptionCode.TEAMCALENDAR_NOT_FOUND;

@Api(tags = "공유 캘린더_일정 관리")
@Validated
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamCalendarController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final TeamCalendarService teamCalendarService;
    private final TeamService teamService;
    private final CalendarService calendarService;

    @ApiOperation(value = "[팀] 일정 생성", notes = "- targets 필드는 회원 식별자 배열을 주세요")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "TEAM_CALENDER_CREATED",
                    content = @Content(schema = @Schema(implementation = TeamCalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "DATE_SET_INVALID / *_FIELD_REQUIRED / *_LENGTH_INVALID / " +
                            "ILLEGAL_STATEMENT_EXPLODEDTEAM / TARGETS_FIELD_REQUIRED / DATE_SET_REQUIRED"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMMEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR")
    })
    @PostMapping("/api/v1/member/team/calendar")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> createTeamCalender(
            HttpServletRequest servletRequest,
            @RequestBody @Valid TeamCalendarSaveRequestDto request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);
        Team team = teamService.findByMember(member);

        teamService.validateIsDeletedTeam(team);
        teamCalendarService.validateTargetExistence(request.getTargets());
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        List<Long> targets = request.getTargets().stream().distinct().collect(Collectors.toList());
        List<Member> targetMembers = teamCalendarService.validateTeamMemberExistence(team, targets);

        TeamCalendar teamCalendar = teamCalendarService.save(request.toEntity(team));

        List<TeamMemberFindResponseDto> childResponses = targetMembers.stream()
                .map(m -> new TeamMemberFindResponseDto(m)).collect(Collectors.toList());

        TeamCalendarDefaultResponseDto response = TeamCalendarDefaultResponseDto.builder()
                .teamCalendar(teamCalendar)
                .targets(childResponses)
                .build();

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_CREATED")
                        .responseMessage("팀 일정 생성 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "[외박] 일정 생성", notes = "- 외박일정은 본인의 것만 생성 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "SLEEPOVER_CALENDAR_CREATED",
                    content = @Content(schema = @Schema(implementation = TeamCalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "ILLEGAL_STATEMENT_EXPLODEDTEAM / DATE_SET_INVALID / DATE_SET_REQUIRED"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR")
    })
    @PostMapping("/api/v1/member/team/calendar/sleepover")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> createSleepoverCalender(
            HttpServletRequest servletRequest,
            @RequestBody @Valid SleepoverCalendarDefaultRequestDto request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);
        Team team = teamService.findByMember(member);

        teamService.validateIsDeletedTeam(team);
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        TeamCalendar teamCalendar = teamCalendarService.save(request.toEntity(team, member.getId()));

        TeamMemberFindResponseDto childResponse = new TeamMemberFindResponseDto(member);

        TeamCalendarDefaultResponseDto response = TeamCalendarDefaultResponseDto.builder()
                .teamCalendar(teamCalendar)
                .targets(new ArrayList<>(Arrays.asList(childResponse)))
                .build();

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("SLEEPOVER_CALENDAR_CREATED")
                        .responseMessage("외박 일정 생성 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "[외박] 일정 수정", notes = "- 본인의 외박일정만 수정 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "SLEEPOVER_CALENDAR_UPDATED",
                    content = @Content(schema = @Schema(implementation = TeamCalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "ILLEGAL_STATEMENT_EXPLODEDTEAM / TEAMCALENDARID_FIELD_REQUIRED / " +
                            "TEAMCALENDARID_NEGATIVEORZERO_INVALID / DATE_SET_INVALID / DATE_SET_REQUIRED"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_SLEEPOVERCALENDAR_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR")
    })
    @PutMapping("/api/v1/member/team/calendar/sleepover")
    public ResponseEntity<DefaultResponseDto<Object>> updateSleepoverCalender(
            HttpServletRequest servletRequest,
            @RequestBody @Valid SleepoverCalendarUpdateRequestDto request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);
        Team team = teamService.findByMember(member);

        teamService.validateIsDeletedTeam(team);
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        TeamCalendar teamCalendar = teamCalendarService.findById(request.getTeamCalendarId());
        teamCalendarService.validateSleepoverCalendarAuthorization(teamCalendar, member);

        teamCalendarService.updateDates(teamCalendar, request);

        TeamMemberFindResponseDto childResponse = new TeamMemberFindResponseDto(member);

        TeamCalendarDefaultResponseDto response = TeamCalendarDefaultResponseDto.builder()
                .teamCalendar(teamCalendar)
                .targets(new ArrayList<>(Arrays.asList(childResponse)))
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("SLEEPOVER_CALENDAR_UPDATED")
                        .responseMessage("외박 일정 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "[팀] 일정 수정", notes = "- targets 필드는 회원 식별자 배열을 주세요")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TEAM_CALENDER_UPDATED",
                    content = @Content(schema = @Schema(implementation = TeamCalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "*_FIELD_REQUIRED / *_LENGTH_INVALID / TEAMCALENDARID_NEGATIVEORZERO_INVALID " +
                            "/ DATE_SET_INVALID / ILLEGAL_STATEMENT_EXPLODEDTEAM / TARGETS_FIELD_REQUIRED / " +
                            "ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR / DATE_SET_REQUIRED"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_TEAMCALENDAR_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMMEMBER_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR")
    })
    @PutMapping("/api/v1/member/team/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> updateTeamCalender(
            HttpServletRequest servletRequest,
            @RequestBody @Valid TeamCalendarUpdateRequestDto request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);
        Team team = teamService.findByMember(member);

        TeamCalendar teamCalendar = teamCalendarService.findById(request.getTeamCalendarId());
        teamCalendarService.validateTeamCalendarAuthorization(team, teamCalendar);
        teamCalendarService.validateSleepoverCalendar(teamCalendar);

        teamService.validateIsDeletedTeam(team);
        teamCalendarService.validateTargetExistence(request.getTargets());
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        List<Long> targets = request.getTargets().stream().distinct().collect(Collectors.toList());
        List<Member> targetMembers = teamCalendarService.validateTeamMemberExistence(team, targets);

        teamCalendarService.update(teamCalendar, request, targets);

        List<TeamMemberFindResponseDto> childResponses = targetMembers.stream()
                .map(m -> new TeamMemberFindResponseDto(m)).collect(Collectors.toList());

        TeamCalendarDefaultResponseDto response = TeamCalendarDefaultResponseDto.builder()
                .teamCalendar(teamCalendar)
                .targets(childResponses)
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_UPDATED")
                        .responseMessage("팀 일정 수정 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "[팀 / 외박] 일정 삭제", notes = "- 팀일정은 팀에 소속된 회원이라면 누구나 삭제 가능합니다.\n" +
            "- 외박 일정은 본인의 것만 삭제 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TEAM_CALENDER_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "TEAMCALENDARID_NEGATIVEORZERO_INVALID / ILLEGAL_STATEMENT_EXPLODEDTEAM"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_TEAMCALENDAR_AUTHORIZATION / FORBIDDEN_SLEEPOVERCALENDAR_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @DeleteMapping("/api/v1/member/team/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> deleteTeamCalender(
            HttpServletRequest servletRequest,
            @RequestParam(value = "teamCalendarId")
            @Positive(message = "삭제할 팀일정 식별자는 양수만 가능합니다.")
            Long teamCalendarId
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);
        Team team = teamService.findByMember(member);

        TeamCalendar teamCalendar = teamCalendarService.findById(teamCalendarId);
        teamCalendarService.validateTeamCalendarAuthorization(team, teamCalendar);
        teamService.validateIsDeletedTeam(team);

        if (teamCalendar.getIsSleepover())
            teamCalendarService.validateSleepoverCalendarAuthorization(teamCalendar, member);

        teamCalendarService.delete(teamCalendar);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_DELETED")
                        .responseMessage("팀 일정 삭제 완료")
                        .build()
                );
    }

    @ApiOperation(value = "[팀 / 외박] 일정 단건 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TEAM_CALENDER_FOUND",
                    content = @Content(schema = @Schema(implementation = TeamCalendarDefaultResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "TEAMCALENDARID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "403",
                    description = "FORBIDDEN_TEAMCALENDAR_AUTHORIZATION"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND / TEAMCALENDAR_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @GetMapping("/api/v1/member/team/calendar")
    public ResponseEntity<DefaultResponseDto<Object>> findTeamCalender(
            HttpServletRequest servletRequest,
            @RequestParam(value = "teamCalendarId")
            @Positive(message = "조회할 팀일정 식별자는 양수만 가능합니다.")
            Long teamCalendarId
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);

        Team team = teamService.findByMember(member);
        TeamCalendar teamCalendar = teamCalendarService.findById(teamCalendarId);
        teamCalendarService.validateTeamCalendarAuthorization(team, teamCalendar);

        List<Member> targetMembers = teamCalendarService.validateTeamMemberExistenceForFind(teamCalendar);

        if (targetMembers == null)
            throw new CustomException(null, TEAMCALENDAR_NOT_FOUND);


        List<TeamMemberFindResponseDto> childResponses = targetMembers.stream()
                .map(m -> new TeamMemberFindResponseDto(m)).collect(Collectors.toList());

        TeamCalendarDefaultResponseDto response = TeamCalendarDefaultResponseDto.builder()
                .teamCalendar(teamCalendar)
                .targets(childResponses)
                .build();

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDER_FOUND")
                        .responseMessage("팀 일정 단건 조회 완료")
                        .data(response)
                        .build()
                );
    }

    @ApiOperation(value = "[팀 / 외박] 일정 월별 조회", notes = "- 종료일이 지난 일정도 전부 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "TEAM_CALENDERS_FOUND",
                    content = @Content(schema = @Schema(implementation = TeamCalendarAbstractResponseDto.class))),
            @ApiResponse(responseCode = "400",
                    description = "YEARMONTH_FIELD_REQUIRED"),
            @ApiResponse(responseCode = "404",
                    description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "500",
                    description = "SERVER_ERROR"),
    })
    @PostMapping("/api/v1/member/team/calendars")
    public ResponseEntity<DefaultResponseDto<Object>> findTeamCalenders(
            HttpServletRequest servletRequest,
            @RequestBody @Valid CalendarFindManyRequestDto request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader("X-AUTH-TOKEN")));
        Member member = memberService.findById(memberId);
        Team team = teamService.findByMember(member);

        List<TeamCalendar> teamCalendars = teamCalendarService.findManyByYearMonth(team, request.getYearMonth());

        List<TeamCalendarAbstractResponseDto> responses = new ArrayList<>();

        for (TeamCalendar teamCalendar : teamCalendars) {
            List<Member> targetMembers = teamCalendarService.validateTeamMemberExistenceForFind(teamCalendar);

            if (targetMembers == null)
                continue;

            List<TeamMemberFindResponseDto> childResponses = targetMembers.stream()
                    .map(targetMember -> new TeamMemberFindResponseDto(targetMember))
                    .collect(Collectors.toList());

            responses.add(new TeamCalendarAbstractResponseDto(teamCalendar, childResponses));
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDERS_FOUND")
                        .responseMessage("팀 일정 월별 조회 완료")
                        .data(responses)
                        .build()
                );
    }
}
