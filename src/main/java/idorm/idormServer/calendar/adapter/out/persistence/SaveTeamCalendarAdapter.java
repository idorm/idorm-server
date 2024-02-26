package idorm.idormServer.calendar.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.calendar.application.port.out.SaveTeamCalendarPort;
import idorm.idormServer.calendar.entity.TeamCalendar;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SaveTeamCalendarAdapter implements SaveTeamCalendarPort {

  private final TeamCalendarRepository teamCalendarRepository;

  @Override
  public void saveTeamCalendar(TeamCalendar teamCalendar) {
    teamCalendarRepository.save(teamCalendar);
  }
}