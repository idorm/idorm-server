package idorm.idormServer.calendar.dto;

import idorm.idormServer.calendar.domain.Calendar;

public class CalendarRequest {

    public Calendar toEntity(Long id) {
        return new Calendar();
    }
}
