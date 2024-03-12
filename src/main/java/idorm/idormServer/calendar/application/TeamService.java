package idorm.idormServer.calendar.application;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.TeamUseCase;
import idorm.idormServer.calendar.application.port.in.dto.TeamParticipantResponse;
import idorm.idormServer.calendar.application.port.in.dto.TeamResponse;
import idorm.idormServer.calendar.application.port.out.DeleteTeamPort;
import idorm.idormServer.calendar.application.port.out.LoadSleepoverCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveTeamPort;
import idorm.idormServer.calendar.entity.SleepoverCalendar;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.entity.Member;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {

	private final LoadMemberPort loadMemberPort;

	private final SaveTeamPort saveTeamPort;
	private final LoadTeamPort loadTeamPort;

	private final LoadSleepoverCalendarPort loadSleepoverCalendarPort;

	private final DeleteTeamPort deleteTeamPort;

	@Override
	@Transactional
	public void addTeamMember(final AuthResponse authResponse, final Long registerMemberId) {
		final Member loginMember = loadMemberPort.loadMember(authResponse.getId());
		final Member registerMember = loadMemberPort.loadMember(registerMemberId);
		final Optional<Team> teamWithOptional = loadTeamPort.findByMemberIdWithOptional(registerMember.getId());

		loginMember.validateExistTeam();

		Team teamDomain = null;
		if (teamWithOptional.isEmpty()) {
			teamDomain = new Team(registerMember);
			teamDomain.addMember(loginMember);

		} else {
			registerMember.getTeam().addMember(loginMember);
			teamDomain = registerMember.getTeam();
		}
		saveTeamPort.save(teamDomain);
	}

	@Override
	@Transactional
	public void deleteMember(final AuthResponse authResponse, final Long targetId) {
		final Member loginMember = loadMemberPort.loadMember(authResponse.getId());
		final Member deleteMember = loadMemberPort.loadMember(targetId);
		final Team team = loadTeamPort.findByMemberId(loginMember.getId());

		team.deleteMember(deleteMember);
	}

	@Override
	public TeamResponse findTeam(final AuthResponse authResponse) {
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());
		final List<SleepoverCalendar> sleepoverCalendars = loadSleepoverCalendarPort.findByToday(team);

		List<Member> members = team.getMembers().stream()
				.filter(member -> member.getMemberStatus().equals("ACTIVE"))
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
	@Transactional
	public void explodeTeam(final AuthResponse authResponse) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());

		team.delete(member);
		deleteTeamPort.deleteTeam(team);
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