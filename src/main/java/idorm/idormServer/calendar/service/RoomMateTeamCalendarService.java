package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.RoomMateTeam;
import idorm.idormServer.calendar.domain.RoomMateTeamCalendar;
import idorm.idormServer.calendar.dto.RoomMateCalendarUpdateRequest;
import idorm.idormServer.calendar.dto.SleepoverCalendarUpdateRequest;
import idorm.idormServer.calendar.repository.RoomMateTeamCalendarRepository;
import idorm.idormServer.exception.CustomException;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static idorm.idormServer.exception.ExceptionCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomMateTeamCalendarService {

    private final RoomMateTeamCalendarRepository teamCalendarRepository;
    private final MemberService memberService;

    /**
     * DB에 팀일정 저장 |
     * 500(SERVER_ERROR)
     */
    @Transactional
    public RoomMateTeamCalendar save(RoomMateTeamCalendar teamCalendar) {
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
    public void update(RoomMateTeamCalendar teamCalendar,
                       RoomMateCalendarUpdateRequest request,
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
    public void updateDates(RoomMateTeamCalendar teamCalendar, SleepoverCalendarUpdateRequest request) {
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
    public void delete(RoomMateTeamCalendar teamCalendar) {
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
    public void deleteTarget(RoomMateTeamCalendar teamCalendar, Long memberId) {
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
    public void deleteManyByContainedTarget(RoomMateTeam team, Member member) {
        try {
            List<RoomMateTeamCalendar> teamCalendars = new ArrayList<>(team.getTeamCalendars());

            if (teamCalendars.size() < 1) return;

            for (RoomMateTeamCalendar teamCalendar : teamCalendars) {

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
    public List<RoomMateTeamCalendar> findManyByYearMonth(RoomMateTeam team, YearMonth yearMonth) {
        try {
            return teamCalendarRepository.findTeamCalendars(team.getId(),
                    yearMonth + "-%", 0);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 외박 일정 월별 조회 |
     * 500(SERVER_ERROR)
     */
    public List<RoomMateTeamCalendar> findSleepOverCalendarsByYearMonth(RoomMateTeam team, YearMonth yearMonth) {
        try {
            return teamCalendarRepository.findTeamCalendars(team.getId(),
                    yearMonth + "-%", 1);
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 오늘의 팀 일정 조회 |
     * 500(SERVER_ERROR)
     */
    public List<RoomMateTeamCalendar> findTeamCalendarsByStartDateIsToday() {
        try {
            return teamCalendarRepository.findTeamCalendarsByStartDateIsToday();
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 단건 조회 |
     * 404(TEAMCALENDAR_NOT_FOUND)
     */
    public RoomMateTeamCalendar findById(Long teamCalendarId) {
        return teamCalendarRepository.findByIdAndIsDeletedIsFalse(teamCalendarId)
                .orElseThrow(() -> {
                    throw new CustomException(null, TEAMCALENDAR_NOT_FOUND);
                });
    }

    /**
     * 팀으로 당일 외박 일정 여부 조회 |
     * 500(SERVER_ERROR)
     */
    public List<Long> findSleepoverYnByTeam(RoomMateTeam team) {
        try {
            List<RoomMateTeamCalendar> todaySleepoverTeamCalendars =
                    teamCalendarRepository.findTodaySleepoverMembersByTeam(team.getId());

            List<Long> members = new ArrayList<>();
            todaySleepoverTeamCalendars.forEach(teamCalendar -> members.add(teamCalendar.getTargets().get(0)));

            return members;
        } catch (RuntimeException e) {
            throw new CustomException(e, SERVER_ERROR);
        }
    }

    /**
     * 팀일정 대상자의 팀 존재 여부 검증 |
     * 저장, 수정, 삭제 시 한다. |
     * 404(TEAMMEMBER_NOT_FOUND)
     */
    public List<Member> validateTeamMemberExistence(RoomMateTeam team, List<Long> targets) {
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
    public List<Member> validateTeamMemberExistenceForFind(RoomMateTeamCalendar teamCalendar) {
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
     * 403(ACCESS_DENIED_TEAM_CALENDAR)
     */
    public void validateTeamCalendarAuthorization(RoomMateTeam team, RoomMateTeamCalendar teamCalendar) {
        if (!teamCalendar.getTeam().equals(team))
            throw new CustomException(null, ACCESS_DENIED_TEAM_CALENDAR);
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
    public void validateSleepoverCalendar(RoomMateTeamCalendar teamCalendar) {
        if (teamCalendar.getIsSleepover())
            throw new CustomException(null, ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR);
    }

    /**
     * 외박일정 수정/삭제 권한 검증 |
     * 403(ACCESS_DENIED_SLEEPOVER_CALENDAR)
     */
    public void validateSleepoverCalendarAuthorization(RoomMateTeamCalendar teamCalendar, Member member) {
        if (!teamCalendar.getTargets().contains(member.getId()))
            throw new CustomException(null, ACCESS_DENIED_SLEEPOVER_CALENDAR);
    }

    /**
     * 외박 일정 날짜 중복 등록 여부 검증
     * 409(DUPLICATE_SLEEPOVER_DATE)
     */
    public void validateDuplicateDate(RoomMateTeamCalendar teamCalendar,
                                      RoomMateTeam team,
                                      Member member,
                                      LocalDate startDate,
                                      LocalDate endDate) {

        String startDateStr = startDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        String endDateStr = endDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));

        List<RoomMateTeamCalendar> teamCalendars = teamCalendarRepository.findTeamCalendarsByDate(team.getId(), startDateStr, endDateStr);

        teamCalendars.removeIf(c -> !c.getTargets().contains(member.getId()));

        if (teamCalendar != null)
            teamCalendars.removeIf(c -> c.getId().equals(teamCalendar.getId()));

        if (teamCalendars.size() > 0)
            throw new CustomException(null, DUPLICATE_SLEEPOVER_DATE);
    }
}
