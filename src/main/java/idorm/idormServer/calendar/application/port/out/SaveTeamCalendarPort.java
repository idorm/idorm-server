package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.TeamCalendar;

public interface SaveTeamCalendarPort {

	void save(TeamCalendar TeamCalendar);
}