package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.adapter.out.exception.NotFoundTeamCalendarException;
import idorm.idormServer.calendar.application.port.out.LoadTeamCalendarPort;
import idorm.idormServer.calendar.domain.Team;
import idorm.idormServer.calendar.domain.TeamCalendar;
import java.time.YearMonth;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadTeamCalendarAdapter implements LoadTeamCalendarPort {

  private final TeamCalendarMapper teamCalendarMapper;
  private final TeamCalendarRepository teamCalendarRepository;

  @Override
  public TeamCalendar findById(Long teamCalendarId) {
    TeamCalendarJpaEntity response = teamCalendarRepository.findById(teamCalendarId)
        .orElseThrow(NotFoundTeamCalendarException::new);
    return teamCalendarMapper.toDomain(response);
  }

  @Override
  public List<TeamCalendar> findByMemberId(Long memberId) {
    List<TeamCalendarJpaEntity> responses = teamCalendarRepository.findByMemberId(memberId);
    return teamCalendarMapper.toDomain(responses);
  }

  @Override
  public List<TeamCalendar> findByTeamId(Long teamId) {
    List<TeamCalendarJpaEntity> responses = teamCalendarRepository.findByTeamId(teamId);
    return teamCalendarMapper.toDomain(responses);
  }

  @Override
  public List<TeamCalendar> findByYearMonth(Team team, YearMonth yearMonth) {
    List<TeamCalendarJpaEntity> responses = teamCalendarRepository.findByYearMonth(
        team.getId(), yearMonth.toString());
    return teamCalendarMapper.toDomain(responses);
  }
}
