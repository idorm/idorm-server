package idorm.idormServer.calendar.application.port.out;

import idorm.idormServer.calendar.entity.SleepoverCalendar;

public interface SaveSleepoverCalendarPort {
    void save(SleepoverCalendar sleepoverCalendar);
}
