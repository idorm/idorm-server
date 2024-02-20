package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.domain.SleepoverCalendar;

public interface SaveSleepoverCalendarPort {
    void save(SleepoverCalendar sleepoverCalendar);
}
