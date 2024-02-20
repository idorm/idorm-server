package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class NotFoundSleepoverCalendarException extends BaseException {

  public NotFoundSleepoverCalendarException() {
    super(CalendarResponseCode.NOT_FOUND_SLEEPOVER_CALENDAR);
  }
}
