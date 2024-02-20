package idorm.idormServer.calendar.application;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.TeamCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.TeamCalendarParticipantResponse;
import idorm.idormServer.calendar.application.port.in.dto.FindOfficialCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveTeamCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.TeamCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateTeamCalendarRequest;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveTeamCalendarPort;
import idorm.idormServer.calendar.domain.Participant;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamCalendarService implements TeamCalendarUseCase {

	private final SaveTeamCalendarPort saveTeamCalendarPort;
	private final LoadTeamCalendarPort loadTeamCalendarPort;

	private final LoadTeamPort loadTeamPort;

	private final LoadMemberPort loadMemberPort;

	@Override
	public TeamCalendarResponse save(final AuthResponse authResponse, final SaveTeamCalendarRequest request) {
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());

		TeamCalendar teamCalendar = request.from(team);
		saveTeamCalendarPort.saveTeamCalendar(teamCalendar);

		return TeamCalendarResponse.of(teamCalendar,
			participants(teamCalendar.getParticipants().getParticipants()));
	}

	@Override
	public TeamCalendarResponse update(final AuthResponse authResponse, final UpdateTeamCalendarRequest request) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());
		final TeamCalendar teamCalendar = loadTeamCalendarPort.findById(request.teamCalendarId());

		teamCalendar.update(team, request.title(), request.content(), request.startDate(), request.endDate(),
			request.startTime(), request.endTime());

		return TeamCalendarResponse.of(teamCalendar,
			participants(teamCalendar.getParticipants().getParticipants()));
	}

	@Override
	public void delete(final AuthResponse authResponse, final Long teamCalendarId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Team team = loadTeamPort.findByMemberId(member.getId());
		final TeamCalendar teamCalendar = loadTeamCalendarPort.findById(teamCalendarId);

		teamCalendar.delete(team);
		// TODO: delete() 안먹힘
	}

	@Override
	public TeamCalendarResponse findById(final AuthResponse authResponse, final Long teamCalendarId) {
		final Member member = loadMemberPort.loadMember(authResponse.getId());
		final Team team = loadTeamPort.findByMemberId(authResponse.getId());

		// TODO: teamCalendarId, teamId로 조회 쿼리. 없으면 AccessDeniedTeamCalendarException throws
		final TeamCalendar teamCalendar = loadTeamCalendarPort.findById(teamCalendarId);

		teamCalendar.validateAuthorization(team);

		return TeamCalendarResponse.of(teamCalendar,
			participants(teamCalendar.getParticipants().getParticipants()));
	}

	@Override
	public List<TeamCalendarResponse> findTeamCalendarsByMonth(final AuthResponse authResponse,
		final FindOfficialCalendarsRequest request) {
		Team team = loadTeamPort.findByMemberId(authResponse.getId());

		teamCalendar.validateAuthorization(team);
		List<TeamCalendar> teamCalendars = loadTeamCalendarPort.findByYearMonth(team, request.yearMonth());

		List<TeamCalendarResponse> responses = teamCalendars.stream()
			.map(teamCalendar -> TeamCalendarResponse.of(teamCalendar,
				participants(teamCalendar.getParticipants().getParticipants())))
			.toList();
		return responses;
	}

	private List<TeamCalendarParticipantResponse> participants(List<Participant> participants) {
		List<Member> members = participants.stream()
			.sorted(Comparator.comparing(Participant::getMemberId))
			.map(participant -> loadMemberPort.loadMember(participant.getMemberId()))
			.toList();

		List<TeamCalendarParticipantResponse> responses = IntStream.rangeClosed(1, members.size())
			.mapToObj(index -> {
				Member member = members.get(index - 1);
				return TeamCalendarParticipantResponse.of(member, index);
			})
			.toList();
		return responses;
	}
}
