package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class IllegalArgumentSleepoverCalendarException extends BaseException {

  public IllegalArgumentSleepoverCalendarException() {
    super(CalendarResponseCode.ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR);
  }
}
