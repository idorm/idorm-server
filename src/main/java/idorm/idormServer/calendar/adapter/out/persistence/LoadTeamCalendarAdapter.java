package idorm.idormServer.calendar.adapter.out.persistence;

import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundTeamCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.entity.Team;
import idorm.idormServer.calendar.entity.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadTeamCalendarAdapter implements LoadTeamCalendarPort {

  private final TeamCalendarRepository teamCalendarRepository;


  @Override
  public TeamCalendar findByIdAndTeamId(Long teamCalendarId, Long teamId) {
    TeamCalendar response = teamCalendarRepository.findByIdAndTeamId(teamCalendarId, teamId)
        .orElseThrow(NotFoundTeamCalendarException::new);
    return response;
  }

  @Override
  public TeamCalendar findByIdAndMemberId(Long teamCalendarId, Long memberId) {
    TeamCalendar response = teamCalendarRepository.findByIdAndMemberId(teamCalendarId, memberId)
        .orElseThrow(NotFoundTeamCalendarException::new);
    return response;
  }

  @Override
  public List<TeamCalendar> findByMemberId(Long memberId) {
    List<TeamCalendar> responses = teamCalendarRepository.findByMemberId(memberId);
    return responses;
  }

  @Override
  public List<TeamCalendar> findByTeamId(Long teamId) {
    List<TeamCalendar> responses = teamCalendarRepository.findByTeamId(teamId);
    return responses;
  }

  @Override
  public List<TeamCalendar> findByYearMonth(Team team, YearMonth yearMonth) {
    List<TeamCalendar> responses = teamCalendarRepository.findByIdAndYearMonth(team.getId(), yearMonth.toString());
    return responses;
  }
}
