package idorm.idormServer.calendar.application;

import idorm.idormServer.calendar.application.port.in.TeamUseCase;
import idorm.idormServer.calendar.application.port.in.dto.OfficialCalendarsFindRequest;
import idorm.idormServer.calendar.application.port.out.AddMemberPort;
import idorm.idormServer.calendar.application.port.out.DeleteParticipantPort;
import idorm.idormServer.calendar.application.port.out.DeleteTeamPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveTeamPort;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {

    private final LoadMemberPort loadMemberPort;
    private final LoadTeamPort loadTeamPort;
    private final DeleteTeamPort deleteTeamPort;
    private final DeleteParticipantPort deleteParticipantPort;
    private final SaveTeamPort saveTeamPort;
    private final AddMemberPort addMemberPort;

    @Override
    public void findAllCalendars(Member member, OfficialCalendarsFindRequest request) {
        Team team = loadTeamPort.load(member);

    }

    @Override
    public void addTeamMember(Member member, Long registerMemberId) {
        Member registerMember = loadMemberPort.load(registerMemberId);

        Team team = loadTeamPort.load(member);
        team.isTeamExists();
        team.participate(registerMember);
        addMemberPort.addMember(registerMember);
        saveTeamPort.saveTeam(team);
    }

    @Override
    public void deleteMember(Member member, Long memberId) {

        Member deleteMember = loadMemberPort.load(memberId);

        Team team = loadTeamPort.load(member);
        team.canRemoveMember(member);
        deleteParticipantPort.deleteMember(member);
        saveTeamPort.saveTeam(team);
    }

    @Override
    public List<Member> findTeamMembers(Member member) {

        Team team = loadTeamPort.load(member);
        List<Member> members = team.getMembers();
        if(members.size() == 1) {
            team.changeTeamStatusToAlone();
        }
        Collections.sort(members, Comparator.comparing(Member::getId));
        return members;

    }

    @Override
    public void isConfirmTeamExploded(Member member) {
        Team team = loadTeamPort.load(member);
        if(team.getTeamStatus().equals("ALNOE")) {
            deleteTeamPort.deleteTeam(team);
        }
        saveTeamPort.saveTeam(team);
    }

}
