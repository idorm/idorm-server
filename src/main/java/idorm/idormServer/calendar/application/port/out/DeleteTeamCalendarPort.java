package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.TeamCalendar;

public interface DeleteTeamCalendarPort {

  void delete(TeamCalendar teamCalendar);
}
