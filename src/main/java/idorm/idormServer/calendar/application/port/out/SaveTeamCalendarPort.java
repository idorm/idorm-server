package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.TeamCalendar;

public interface SaveTeamCalendarPort {
    void saveTeamCalendar(TeamCalendar TeamCalendar);
}
