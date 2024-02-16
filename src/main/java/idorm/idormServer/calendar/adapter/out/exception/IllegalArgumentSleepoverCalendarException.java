package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class IllegalArgumentSleepoverCalendarException extends BaseException {

  public IllegalArgumentSleepoverCalendarException() {
    super(CalendarResponseCode.ILLEGAL_ARGUMENT_SLEEPOVERCALENDAR);
  }
}
