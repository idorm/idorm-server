package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CalendarServiceFacade {

    private final TeamService teamService;
    private final TeamCalendarService teamCalendarService;

    public void addTeamMember(Team team, Member loginMember, Member registerMember) {
        if (team != null) { // 룸메이트 초대
            teamService.validateTeamFull(team);
            teamService.validateIsDeletedTeam(team);
            teamService.addMember(team, registerMember);
        } else { // 팀 생성 후 룸메이트 초대
            Team createdTeam = teamService.create(loginMember);
            teamService.addMember(createdTeam, registerMember);
        }
    }

    public void deleteTeamMember(Team team, Member member) {
        teamCalendarService.deleteManyByContainedTarget(team, member);
        teamService.removeMember(team, member);

        if (team.getMemberCount() < 1) { // 팀 삭제
            teamService.delete(team);
        } else if (team.getMemberCount() == 1) { // 팀 폭발여부 변경
            teamService.updateIsNeedToConfirmDeleted(team);
        }
    }
}
