package idorm.idormServer.calendar.adapter.out.persistence;

import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import idorm.idormServer.calendar.domain.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteTeamCalendarAdapter implements DeleteTeamCalendarPort {

  private final TeamCalendarMapper teamCalendarMapper;
  private final TeamCalendarRepository teamCalendarRepository;

  @Override
  public void delete(TeamCalendar teamCalendar) {
    teamCalendarRepository.delete(teamCalendarMapper.toEntity(teamCalendar));
  }
}
