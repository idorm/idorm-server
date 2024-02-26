package idorm.idormServer.calendar.application;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.TeamUseCase;
import idorm.idormServer.calendar.application.port.in.dto.TeamParticipantResponse;
import idorm.idormServer.calendar.application.port.in.dto.TeamResponse;
import idorm.idormServer.calendar.application.port.out.DeleteSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveTeamPort;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {

	private final LoadMemberPort loadMemberPort;

	private final SaveTeamPort saveTeamPort;
	private final LoadTeamPort loadTeamPort;

	private final LoadSleepoverCalendarPort loadSleepoverCalendarPort;
	private final DeleteSleepoverCalendarPort deleteSleepoverCalendarPort;

	private final LoadTeamCalendarPort loadTeamCalendarPort;
	private final DeleteTeamCalendarPort deleteTeamCalendarPort;

	@Override
	@Transactional
	public void addTeamMember(final AuthResponse authResponse, final Long registerMemberId) {
		final Member loginMember = loadMemberPort.loadMember(authResponse.getId()); // 두 번째 팀원 (초대를 수락한 사람) : 팀이 없어야 함
		final Member registerMember = loadMemberPort.loadMember(
			registerMemberId); // 첫 번째 팀원 (초대한 사람) :: 팀이 있거나(두번 째 팀원 초대), 없어야 함(팀이 생성).
		final Optional<Team> teamWithOptional = loadTeamPort.findByMemberIdWithOptional(registerMember.getId());

		// TODO: loginMember가 팀이 없어야 함. 팀이 있으면 예외를 던져야 함. (Query exists로 변경)
		// 	if(loginMember.getTeamDomain().isPresent()){
		// 		throw new AlreadyRegisteredTeamException();
		// 	}

		Team teamDomain = null;
		if (teamWithOptional.isEmpty()) { // 팀 생성 되는 경우
			List<Member> members = List.of(registerMember, loginMember);
			teamDomain = new Team(members);
		} else { // 팀 존재하는 경우. (두 번째 팀원 초대)
			teamDomain = teamWithOptional.get();
			teamDomain.addMember(loginMember);
		}
		saveTeamPort.save(teamDomain);
	}

	@Override
	@Transactional
	public void deleteMember(final AuthResponse authResponse, final Long targetId) {
		final Member loginMember = loadMemberPort.loadMember(authResponse.getId());
		final Member deleteMember = loadMemberPort.loadMember(targetId);
		final Team teamDomain = loadTeamPort.findByMemberIdWithTeamMember(loginMember.getId());

		teamDomain.deleteMember(deleteMember);
	}

	@Override
	public TeamResponse findTeam(final AuthResponse authResponse) {
		final Team team = loadTeamPort.findByMemberIdWithTeamMember(authResponse.getId());
		final List<SleepoverCalendar> sleepoverCalendars = loadSleepoverCalendarPort.findByToday(team);

		List<Member> members = team.getMembers().stream()
			.sorted(Comparator.comparing(Member::getId))
			.toList();

		List<TeamParticipantResponse> responses = IntStream.rangeClosed(1, members.size())
			.mapToObj(index -> {
				Member member = members.get(index - 1);
				return TeamParticipantResponse.of(member, index,
					isTodaySleepover(member.getId(), sleepoverCalendars));
			})
			.toList();
		return TeamResponse.of(team, responses);
	}

	@Override
	public void explodeTeam(final AuthResponse authResponse) {
		final Team teamDomain = loadTeamPort.findByMemberIdWithTeamMember(authResponse.getId());
		final List<TeamCalendar> teamCalendars = loadTeamCalendarPort.findByTeamId(teamDomain.getId());
		final List<SleepoverCalendar> sleepoverCalendars = loadSleepoverCalendarPort.findByTeamId(
			teamDomain.getId());

		teamDomain.delete();
		teamCalendars.forEach(deleteTeamCalendarPort::delete);
		sleepoverCalendars.forEach(deleteSleepoverCalendarPort::delete);
	}

	private boolean isTodaySleepover(Long memberId, List<SleepoverCalendar> sleepoverCalendars) {
		boolean isTodaySleepover = sleepoverCalendars.stream()
			.anyMatch(sleepoverCalendar -> isParticipantInSleepover(memberId, sleepoverCalendar));
		return isTodaySleepover;
	}

	private boolean isParticipantInSleepover(Long memberId, SleepoverCalendar sleepoverCalendar) {
		return sleepoverCalendar.getMemberId().equals(memberId);
	}

}