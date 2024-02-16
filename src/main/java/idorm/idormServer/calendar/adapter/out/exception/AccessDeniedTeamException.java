package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedTeamException extends BaseException {

  public AccessDeniedTeamException() {
    super(CalendarResponseCode.ACCESS_DENIED_SLEEPOVER_CALENDAR);
  }
}
