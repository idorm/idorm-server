package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class NotFoundCalendarException extends BaseException {

  public NotFoundCalendarException() {
    super(CalendarResponseCode.NOT_FOUND_CALENDAR);
  }
}
