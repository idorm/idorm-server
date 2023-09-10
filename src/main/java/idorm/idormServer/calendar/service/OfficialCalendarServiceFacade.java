package idorm.idormServer.calendar.service;

import idorm.idormServer.calendar.domain.RoomMateTeam;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OfficialCalendarServiceFacade {

    private final RoomMateTeamService teamService;
    private final RoomMateTeamCalendarService teamCalendarService;

    public void addTeamMember(RoomMateTeam team, Member loginMember, Member registerMember) {
        if (team != null) { // 룸메이트 초대
            teamService.validateTeamFull(team);
            teamService.validateIsDeletedTeam(team);
            teamService.addMember(team, registerMember);
        } else { // 팀 생성 후 룸메이트 초대
            RoomMateTeam createdTeam = teamService.create(registerMember);
            teamService.addMember(createdTeam, loginMember);
        }
    }

    public void deleteTeamMember(RoomMateTeam team, Member member) {
        teamCalendarService.deleteManyByContainedTarget(team, member);
        teamService.removeMember(team, member);

        if (team.getMemberCount() < 1) { // 팀 삭제
            teamService.delete(team);
        } else if (team.getMemberCount() == 1) { // 팀 폭발여부 변경
            teamService.updateIsNeedToConfirmDeleted(team);
        }
    }
}
