package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedSleepoverCalendarException extends BaseException {

  public AccessDeniedSleepoverCalendarException() {
    super(CalendarResponseCode.ACCESS_DENIED_SLEEPOVER_CALENDAR);
  }
}
