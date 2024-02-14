package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.OfficialCalendar;

public interface SaveOfficialCalendarPort {
    void save(OfficialCalendar officialCalendarJpaEntity);
}
