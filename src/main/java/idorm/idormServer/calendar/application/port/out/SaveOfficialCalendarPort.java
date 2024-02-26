package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.OfficialCalendar;

public interface SaveOfficialCalendarPort {
    void save(OfficialCalendar officialCalendar);
}
