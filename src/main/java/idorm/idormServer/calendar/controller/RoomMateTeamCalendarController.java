package idorm.idormServer.calendar.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.calendar.domain.RoomMateTeam;
import idorm.idormServer.calendar.domain.RoomMateTeamCalendar;
import idorm.idormServer.calendar.dto.*;
import idorm.idormServer.calendar.service.OfficialCalendarService;
import idorm.idormServer.calendar.service.RoomMateTeamCalendarService;
import idorm.idormServer.calendar.service.RoomMateTeamService;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import idorm.idormServer.photo.service.MemberPhotoService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;
import static idorm.idormServer.config.SecurityConfiguration.AUTHENTICATION_HEADER_NAME;
import static idorm.idormServer.exception.ExceptionCode.TEAMCALENDAR_NOT_FOUND;

@Tag(name = "4. RoomMate Team Calendar", description = "룸메이트 팀 일정 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMateTeamCalendarController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final MemberPhotoService memberPhotoService;
    private final RoomMateTeamCalendarService teamCalendarService;
    private final RoomMateTeamService teamService;
    private final OfficialCalendarService calendarService;

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

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMember(member);

        teamService.validateIsDeletedTeam(team);
        teamCalendarService.validateTargetExistence(request.getTargets());
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        List<Long> targets = request.getTargets().stream().distinct().collect(Collectors.toList());
        List<Member> targetMembers = teamCalendarService.validateTeamMemberExistence(team, targets);

        RoomMateTeamCalendar teamCalendar = teamCalendarService.save(request.toEntity(team));

        List<RoomMateResponse> childResponses = targetMembers.stream()
                .map(m -> new RoomMateResponse(m, memberPhotoService.findByMember(m))).collect(Collectors.toList());

        RoomMateCalendarResponse response = RoomMateCalendarResponse.builder()
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
            HttpServletRequest servletRequest,
            @RequestBody @Valid SleepoverCalendarRequest request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMember(member);

        teamService.validateIsDeletedTeam(team);
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        teamCalendarService.validateDuplicateDate(null,
                team,
                member,
                request.getStartDate(),
                request.getEndDate());

        RoomMateTeamCalendar teamCalendar = teamCalendarService.save(request.toEntity(team, member.getId()));

        RoomMateResponse childResponse = new RoomMateResponse(member, memberPhotoService.findByMember(member));

        RoomMateCalendarResponse response = RoomMateCalendarResponse.builder()
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
            HttpServletRequest servletRequest,
            @RequestBody @Valid SleepoverCalendarUpdateRequest request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMember(member);

        teamService.validateIsDeletedTeam(team);
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());
        RoomMateTeamCalendar teamCalendar = teamCalendarService.findById(request.getTeamCalendarId());
        teamCalendarService.validateSleepoverCalendarAuthorization(teamCalendar, member);

        teamCalendarService.validateDuplicateDate(teamCalendar,
                team,
                member,
                request.getStartDate(),
                request.getEndDate());

        teamCalendarService.updateDates(teamCalendar, request);

        RoomMateResponse childResponse = new RoomMateResponse(member, memberPhotoService.findByMember(member));

        RoomMateCalendarResponse response = RoomMateCalendarResponse.builder()
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

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMember(member);

        RoomMateTeamCalendar teamCalendar = teamCalendarService.findById(request.getTeamCalendarId());
        teamCalendarService.validateTeamCalendarAuthorization(team, teamCalendar);
        teamCalendarService.validateSleepoverCalendar(teamCalendar);

        teamService.validateIsDeletedTeam(team);
        teamCalendarService.validateTargetExistence(request.getTargets());
        calendarService.validateStartAndEndDate(request.getStartDate(), request.getEndDate());

        List<Long> targets = request.getTargets().stream().distinct().collect(Collectors.toList());
        List<Member> targetMembers = teamCalendarService.validateTeamMemberExistence(team, targets);

        teamCalendarService.update(teamCalendar, request, targets);

        List<RoomMateResponse> childResponses = targetMembers.stream()
                .map(m -> new RoomMateResponse(m, memberPhotoService.findByMember(m))).collect(Collectors.toList());

        RoomMateCalendarResponse response = RoomMateCalendarResponse.builder()
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

    @Operation(summary = "[팀 / 외박] 일정 삭제", description = "- 팀일정은 팀에 소속된 회원이라면 누구나 삭제 가능합니다.\n" +
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

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMember(member);

        RoomMateTeamCalendar teamCalendar = teamCalendarService.findById(teamCalendarId);
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

    @Operation(summary = "[팀 / 외박] 일정 단건 조회")
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

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME
        )));
        Member member = memberService.findById(memberId);

        RoomMateTeam team = teamService.findByMember(member);
        RoomMateTeamCalendar teamCalendar = teamCalendarService.findById(teamCalendarId);
        teamCalendarService.validateTeamCalendarAuthorization(team, teamCalendar);

        List<Member> targetMembers = teamCalendarService.validateTeamMemberExistenceForFind(teamCalendar);

        if (targetMembers == null)
            throw new CustomException(null, TEAMCALENDAR_NOT_FOUND);


        List<RoomMateResponse> childResponses = targetMembers.stream()
                .map(m -> new RoomMateResponse(m, memberPhotoService.findByMember(m))).collect(Collectors.toList());

        RoomMateCalendarResponse response = RoomMateCalendarResponse.builder()
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

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMember(member);

        List<RoomMateTeamCalendar> teamCalendars = teamCalendarService.findManyByYearMonth(team, request.getYearMonth());

        List<RoomMateCalendarSummaryResponse> responses = new ArrayList<>();

        for (RoomMateTeamCalendar teamCalendar : teamCalendars) {
            List<Member> targetMembers = teamCalendarService.validateTeamMemberExistenceForFind(teamCalendar);

            if (targetMembers == null)
                continue;

            List<RoomMateResponse> childResponses = targetMembers.stream()
                    .map(targetMember -> new RoomMateResponse(targetMember, memberPhotoService.findByMember(targetMember)))
                    .collect(Collectors.toList());

            responses.add(new RoomMateCalendarSummaryResponse(teamCalendar, childResponses));
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_CALENDERS_FOUND")
                        .responseMessage("팀 일정 월별 조회 완료")
                        .data(responses)
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
            HttpServletRequest servletRequest,
            @RequestBody @Valid SleepoverCalendarsFindRequest request
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        Member searchMember = memberService.findById(request.getMemberId());

        RoomMateTeam loginMemberTeam = teamService.findByMember(member);

        if (!member.equals(searchMember))
            teamService.validateTeamMember(loginMemberTeam, searchMember);

        List<RoomMateTeamCalendar> teamCalendars = teamCalendarService.findSleepOverCalendarsByYearMonth(loginMemberTeam,
                request.getYearMonth());

        List<RoomMateTeamCalendar> responseCalendars = new ArrayList<>();

        for (RoomMateTeamCalendar c : teamCalendars) {
            if (c.getEndDate().isBefore(LocalDateTime.now().plusHours(9).toLocalDate()))
                continue;
            if (!c.getTargets().contains(searchMember.getId()))
                continue;

            responseCalendars.add(c);
        }

        List<SleepoverCalendarResponse> responses = responseCalendars.stream()
                .map(c -> new SleepoverCalendarResponse(c))
                .collect(Collectors.toList());

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_SLEEPOVER_CALENDERS_FOUND")
                        .responseMessage("팀 외박 일정 월별 조회 완료")
                        .data(responses)
                        .build()
                );
    }
}
