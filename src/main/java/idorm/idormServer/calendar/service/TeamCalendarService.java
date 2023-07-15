package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.calendar.dto.TeamCalendar.SleepoverCalendarUpdateRequestDto;
import idorm.idormServer.calendar.dto.TeamCalendar.TeamCalendarUpdateRequestDto;
import idorm.idormServer.calendar.repository.TeamCalendarRepository;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamCalendarService {

    private final TeamCalendarRepository teamCalendarRepository;
    private final MemberService memberService;

    /**
     * DB에 팀일정 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public TeamCalendar save(TeamCalendar teamCalendar) {
        try {
            return teamCalendarRepository.save(teamCalendar);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void update(TeamCalendar teamCalendar,
                       TeamCalendarUpdateRequestDto request,
                       List<Long> targets) {
        try {
            teamCalendar.updateContents(request, targets);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 외박일정 수정 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void updateDates(TeamCalendar teamCalendar, SleepoverCalendarUpdateRequestDto request) {
        try {
            teamCalendar.updateDates(request);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void delete(TeamCalendar teamCalendar) {
        try {
            teamCalendar.delete();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 대상 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteTarget(TeamCalendar teamCalendar, Long memberId) {
        try {
            teamCalendar.deleteTarget(memberId);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 특정 회원을 대상으로 설정된 팀일정 다건 삭제 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public void deleteManyByContainedTarget(Team team, Member member) {
        try {
            List<TeamCalendar> teamCalendars = new ArrayList<>(team.getTeamCalendars());

            if (teamCalendars.size() < 1) return;

            for (TeamCalendar teamCalendar : teamCalendars) {

                List<Long> targetsId = teamCalendar.getTargets();

                if (!targetsId.contains(member.getId())) {
                    continue;
                } else if (targetsId.size() == 1){
                    teamCalendar.deleteTarget(member.getId());
                    teamCalendar.delete();
                } else {
                    teamCalendar.deleteTarget(member.getId());
                }
            }
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 월별 조회 |
     * 500(SERVER_ERROR)
     */
    public List<TeamCalendar> findManyByYearMonth(Team team, YearMonth yearMonth) {
        try {
            return teamCalendarRepository.findByTeamAndIsDeletedIsFalseAndDateLike(team.getId(),
                    yearMonth + "-%");
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 단건 조회 |
     * 404(TEAMCALENDAR_NOT_FOUND)
     */
    public TeamCalendar findById(Long teamCalendarId) {
        return teamCalendarRepository.findByIdAndIsDeletedIsFalse(teamCalendarId)
                .orElseThrow(() -> {
                    throw new CustomException(null, TEAMCALENDAR_NOT_FOUND);
                });
    }


    /**
     * 팀일정 대상자의 팀 존재 여부 검증 |
     * 저장, 수정, 삭제 시 한다. |
     * 404(TEAMMEMBER_NOT_FOUND)
     */
    public List<Member> validateTeamMemberExistence(Team team, List<Long> targets) {
        List<Member> teamMembers = team.getMembers();
        List<Member> targetMembers = new ArrayList<>();

        for (Long target : targets) {
            Member targetMember = memberService.findById(target);
            if (!teamMembers.contains(targetMember))
                throw new CustomException(null, TEAMMEMBER_NOT_FOUND);
            targetMembers.add(targetMember);
        }
        return targetMembers;
    }

    /**
     * 팀일정 대상자의 팀 존재 여부 검증 |
     * 조회 시 사용한다. 더이상 존재하지 않는 대상자라면, 해당 대상자 삭제 후 응답한다.
     * 해당 팀일정에 대상자가 존재하지 않으면, 팀일정을 삭제 후 null을 리턴한다.
     * 404(DELETED_TEAMCALENDAR)
     */
    @Transactional
    public List<Member> validateTeamMemberExistenceForFind(TeamCalendar teamCalendar) {
        List<Long> targets = teamCalendar.getTargets().stream().collect(Collectors.toList());
        List<Member> teamMembers = teamCalendar.getTeam().getMembers();
        List<Member> returnMembers = new ArrayList<>();

        for (Long target : targets) {
            Optional<Member> targetMember = memberService.findByIdOptional(target);

            if (targetMember.isEmpty()) {
                this.deleteTarget(teamCalendar, target);
                continue;
            }

            if (!teamMembers.contains(targetMember.get())) {
                this.deleteTarget(teamCalendar, target);
                continue;
            }
            returnMembers.add(targetMember.get());
        }

        if (returnMembers.size() < 1) {
            this.delete(teamCalendar);
            return null;
        }

        return returnMembers;
    }

    /**
     * 팀일정 수정 권한 검증 |
     * 403(FORBIDDEN_TEAMCALENDAR_AUTHORIZATION)
     */
    public void validateTeamCalendarAuthorization(Team team, TeamCalendar teamCalendar) {
        if (!teamCalendar.getTeam().equals(team))
            throw new CustomException(null, FORBIDDEN_TEAMCALENDAR_AUTHORIZATION);
    }

    /**
     * 대상자 존재 여부 검증 |
     * 400(TARGETS_FIELD_REQUIRED)
     */
    public void validateTargetExistence(List<Long> targets) {
        if (targets.size() < 1)
            throw new CustomException(null, TARGETS_FIELD_REQUIRED);
    }

    /**
     * 외박일정 여부 검증 |
     * 400(ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR)
     */
    public void validateSleepoverCalendar(TeamCalendar teamCalendar) {
        if (teamCalendar.getIsSleepover())
            throw new CustomException(null, ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR);
    }

    /**
     * 외박일정 수정/삭제 권한 검증 |
     * 403(FORBIDDEN_SLEEPOVERCALENDAR_AUTHORIZATION)
     */
    public void validateSleepoverCalendarAuthorization(TeamCalendar teamCalendar, Member member) {
        if (!teamCalendar.getTargets().contains(member.getId()))
            throw new CustomException(null, FORBIDDEN_SLEEPOVERCALENDAR_AUTHORIZATION);
    }
}
