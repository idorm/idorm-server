package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundCalendarException extends BaseException {

  public NotFoundCalendarException() {
    super(CalendarResponseCode.NOT_FOUND_CALENDAR);
  }
}
