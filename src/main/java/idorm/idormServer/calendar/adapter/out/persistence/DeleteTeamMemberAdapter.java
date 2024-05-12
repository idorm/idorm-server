package idorm.idormServer.calendar.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.DeleteSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.DeleteTeamMemberPort;
import idorm.idormServer.calendar.application.port.out.DeleteTeamPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.member.entity.Member;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteTeamMemberAdapter implements DeleteTeamMemberPort {

	private final LoadTeamPort loadTeamPort;
	private final DeleteTeamPort deleteTeamPort;

	private final LoadTeamCalendarPort loadTeamCalendarPort;
	private final DeleteTeamCalendarPort deleteTeamCalendarPort;

	private final DeleteSleepoverCalendarPort deleteSleepoverCalendarPort;

	@Override
	public void deleteTeamMember(Member member) {
		final Team team = loadTeamPort.findByMemberId(member.getId());
		final List<TeamCalendar> teamCalendars = loadTeamCalendarPort.findByMemberId(member.getId());

		team.deleteMember(member);
		if (team.getMembers().isEmpty()) {
			deleteSleepoverCalendarPort.deleteByMemberId(member.getId());
			deleteTeamPort.delete(team);
		} else {
			deleteSleepoverCalendarPort.deleteByMemberId(member.getId());
			teamCalendars
				.forEach(teamCalendar -> {
					teamCalendar.deletePariticipant(member.getId());
					if (teamCalendar.getParticipants().isEmpty())
						deleteTeamCalendarPort.delete(teamCalendar);
				});
		}
	}
}