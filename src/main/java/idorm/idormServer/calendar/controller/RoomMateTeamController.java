package idorm.idormServer.calendar.controller;

import idorm.idormServer.auth.JwtTokenProvider;
import idorm.idormServer.calendar.domain.RoomMateTeam;
import idorm.idormServer.calendar.dto.RoomMateFullResponse;
import idorm.idormServer.calendar.dto.RoomMatesFullResponse;
import idorm.idormServer.calendar.service.OfficialCalendarServiceFacade;
import idorm.idormServer.calendar.service.RoomMateTeamCalendarService;
import idorm.idormServer.calendar.service.RoomMateTeamService;
import idorm.idormServer.common.DefaultResponseDto;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
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
import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static idorm.idormServer.config.SecurityConfiguration.API_ROOT_URL_V1;
import static idorm.idormServer.config.SecurityConfiguration.AUTHENTICATION_HEADER_NAME;

@Tag(name = "RoomMateTeam", description = "룸메이트 팀원 관리 api")
@Validated
@RestController
@RequestMapping(API_ROOT_URL_V1)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMateTeamController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final RoomMateTeamService teamService;
    private final OfficialCalendarServiceFacade calendarServiceFacade;
    private final RoomMateTeamCalendarService teamCalendarService;

    @Operation(summary = "룸메이트 초대 수락", description = "- 초대를 보낼 때가 아닌, 초대를 수락할 때 요청을 보내주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "TEAM_MEMBER_CREATED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400",
                    description = "- REGISTERMEMBERID_NEGATIVEORZERO_INVALID\n- ILLEGAL_STATEMENT_EXPLODEDTEAM\n" +
                            "- ILLEGAL_ARGUMENT_ADMIN"),
            @ApiResponse(responseCode = "404",
                    description = "- MEMBER_NOT_FOUND\n- TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "409",
                    description = "- DUPLICATE_TEAM\n- CANNOT_REGISTER_TEAM_STATUS_FULL"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PostMapping("/member/team")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DefaultResponseDto<Object>> addTeamMember(
            HttpServletRequest servletRequest,
            @RequestParam(value = "registerMemberId")
            @Positive(message = "회원 식별자는 양수만 가능합니다.")
            Long registerMemberId
    ) {

        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        Member registerMember = memberService.findById(registerMemberId);

        memberService.validateTargetSelf(member, registerMember);
        memberService.validateTargetAdmin(registerMember);
        teamService.validateTeamExistence(registerMember);

        RoomMateTeam team = teamService.findByMemberOptional(member);

        calendarServiceFacade.addTeamMember(team, member, registerMember);

        return ResponseEntity.status(201)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_MEMBER_CREATED")
                        .responseMessage("팀 회원 초대 완료")
                        .build()
                );
    }

    @Operation(summary = "팀원 삭제", description = "- 본인 혹은 팀의 다른 회원을 팀에서 삭제시킬 때 사용합니다.\n" +
            "- 로그인한 유저가 팀이 없는 경우는 404(TEAM_NOT_FOUND) 를 응답합니다.\n" +
            "- 다른 팀원을 삭제하려는 경우, 삭제하려는 팀원의 팀이 없거나 같은 팀이 아닐 경우 403(ACCESS_DENIED_TEAM) 을 응답합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_MEMBER_DELETED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "400", description = "MEMBERID_NEGATIVEORZERO_INVALID"),
            @ApiResponse(responseCode = "403", description = "ACCESS_DENIED_TEAM"),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND / TEAM_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @DeleteMapping("/member/team")
    public ResponseEntity<DefaultResponseDto<Object>> deleteTeamMember(
            HttpServletRequest servletRequest,
            @RequestParam(value = "memberId")
            @Positive(message = "삭제할 회원 식별자는 양수만 가능합니다.")
            Long memberId
    ) {

        long loginMemberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member loginMember = memberService.findById(loginMemberId);
        Member deleteMember = memberService.findById(memberId);

        RoomMateTeam loginMemberTeam = teamService.findByMember(loginMember);

        if (!loginMember.equals(deleteMember)) {
            teamService.validateTeamMember(loginMemberTeam, deleteMember);
        }

        calendarServiceFacade.deleteTeamMember(loginMemberTeam, deleteMember);

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_MEMBER_DELETED")
                        .responseMessage("팀 회원 삭제 완료")
                        .build()
                );
    }

    @Operation(summary = "팀원 전체 조회", description = "- 팀에 소속된 팀원이 1명이라면, isNeedToConfirmDeleted: true 로 응답합니다. \n" +
            "- 남은 팀원 1명이 팀 폭발 여부를 확인했다면, 팀 폭발 인지 OK 요청 을 서버에게 보내주세요. \n" +
            "- 팀원이 없다면 팀 식별자(teamId)가 -999 이고, members는 로그인한 회원만 담은 배열로 응답합니다. \n" +
            "- json 내부의 회원 배열에서, 회원의 order는 0부터 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_MEMBERS_FOUND",
                    content = @Content(schema = @Schema(implementation = RoomMatesFullResponse.class))),
            @ApiResponse(responseCode = "404", description = "MEMBER_NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR"),
    })
    @GetMapping("/member/team")
    public ResponseEntity<DefaultResponseDto<Object>> findTeamMembers(
            HttpServletRequest servletRequest
    ) {
        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMemberOptional(member);

        RoomMatesFullResponse responses = null;

        if (team == null) {

            responses = new RoomMatesFullResponse(-999L,
                    false,
                    new ArrayList<>(Arrays.asList(new RoomMateFullResponse(member, null))));
        } else {
            List<Member> members = teamService.findTeamMembers(team);

            if (members.size() < 2) {
                teamService.updateIsNeedToConfirmDeleted(team);
                responses = new RoomMatesFullResponse(team.getId(),
                        true,
                        new ArrayList<>(Arrays.asList(new RoomMateFullResponse(member, null))));
            } else {
                List<Long> sleepoverMembers = teamCalendarService.findSleepoverYnByTeam(team);

                List<RoomMateFullResponse> childResponses = members.stream()
                        .map(m -> new RoomMateFullResponse(m, sleepoverMembers.contains(m.getId())))
                        .collect(Collectors.toList());

                responses = new RoomMatesFullResponse(team.getId(),
                        false,
                        childResponses);
            }
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_MEMBERS_FOUND")
                        .responseMessage("팀 회원 다건 조회 완료")
                        .data(responses)
                        .build()
                );
    }

    @Operation(summary = "팀 폭발 확인 OK", description = "- 최후의 남은 팀원 1명이 팀 폭발했음을 확인했을 때 해당 API를 요청해주세요.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "TEAM_EXPLODED_CHECKED",
                    content = @Content(schema = @Schema(implementation = Object.class))),
            @ApiResponse(responseCode = "409", description = "CANNOT_EXPLODE_TEAM"),
            @ApiResponse(responseCode = "500", description = "SERVER_ERROR")
    })
    @PatchMapping("/member/team")
    public ResponseEntity<DefaultResponseDto<Object>> isConfirmTeamExploded(
            HttpServletRequest servletRequest
    ) {
        long memberId = Long.parseLong(jwtTokenProvider.getUsername(servletRequest.getHeader(AUTHENTICATION_HEADER_NAME)));
        Member member = memberService.findById(memberId);
        RoomMateTeam team = teamService.findByMemberOptional(member);

        if (team == null) {
            teamService.removeMember(team, member);
        } else {
            teamService.validateReadyToDeleteTeam(team);
            teamService.delete(team);
        }

        return ResponseEntity.status(200)
                .body(DefaultResponseDto.builder()
                        .responseCode("TEAM_EXPLODED_CHECKED")
                        .responseMessage("팀 삭제 완료")
                        .build()
                );
    }
}
