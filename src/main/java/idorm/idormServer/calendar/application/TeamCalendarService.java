package idorm.idormServer.calendar.application;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.calendar.application.port.in.TeamCalendarUseCase;
import idorm.idormServer.calendar.application.port.in.dto.FindOfficialCalendarsRequest;
import idorm.idormServer.calendar.application.port.in.dto.SaveTeamCalendarRequest;
import idorm.idormServer.calendar.application.port.in.dto.TeamCalendarParticipantResponse;
import idorm.idormServer.calendar.application.port.in.dto.TeamCalendarResponse;
import idorm.idormServer.calendar.application.port.in.dto.UpdateTeamCalendarRequest;
import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.application.port.out.LoadTeamPort;
import idorm.idormServer.calendar.application.port.out.SaveTeamCalendarPort;
import idorm.idormServer.calendar.domain.Participant;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamCalendarService implements TeamCalendarUseCase {

  private final SaveTeamCalendarPort saveTeamCalendarPort;
  private final LoadTeamCalendarPort loadTeamCalendarPort;
  private final DeleteTeamCalendarPort deleteTeamCalendarPort;

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
    final TeamCalendar teamCalendar = loadTeamCalendarPort.findByIdAndMemberId(request.teamCalendarId(),
        authResponse.getId());
    final Team team = loadTeamPort.findByMemberId(authResponse.getId());

    teamCalendar.update(team, request.title(), request.content(), request.getPeriod(),
        request.startTime(), request.endTime());

    return TeamCalendarResponse.of(teamCalendar,
        participants(teamCalendar.getParticipants().getParticipants()));
  }

  @Override
  public void delete(final AuthResponse authResponse, final Long teamCalendarId) {
    final TeamCalendar teamCalendar = loadTeamCalendarPort.findByIdAndMemberId(teamCalendarId, authResponse.getId());

    deleteTeamCalendarPort.delete(teamCalendar);
  }

  @Override
  public TeamCalendarResponse findById(final AuthResponse authResponse, final Long teamCalendarId) {
    final Team team = loadTeamPort.findByMemberId(authResponse.getId());

    final TeamCalendar teamCalendar = loadTeamCalendarPort.findByIdAndTeamId(teamCalendarId, team.getId());

    return TeamCalendarResponse.of(teamCalendar,
        participants(teamCalendar.getParticipants().getParticipants()));
  }

  @Override
  public List<TeamCalendarResponse> findTeamCalendarsByMonth(final AuthResponse authResponse,
      final FindOfficialCalendarsRequest request) {
    final Member member = loadMemberPort.loadMember(authResponse.getId());
    final Team team = loadTeamPort.findByMemberId(authResponse.getId());

    team.validateTeamAccessMember(member);
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
