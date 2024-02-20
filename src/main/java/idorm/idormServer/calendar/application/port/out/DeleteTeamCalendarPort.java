package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.TeamCalendar;

public interface DeleteTeamCalendarPort {

  void delete(TeamCalendar teamCalendar);
}
