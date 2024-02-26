package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.DeleteTeamCalendarPort;
import idorm.idormServer.calendar.entity.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteTeamCalendarAdapter implements DeleteTeamCalendarPort {

  private final TeamCalendarRepository teamCalendarRepository;

  @Override
  public void delete(TeamCalendar teamCalendar) {
    teamCalendarRepository.delete(teamCalendar);
  }
}
