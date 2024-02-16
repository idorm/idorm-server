package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedSleepoverCalendarException extends BaseException {

  public AccessDeniedSleepoverCalendarException() {
    super(CalendarResponseCode.ACCESS_DENIED_SLEEPOVER_CALENDAR);
  }
}
