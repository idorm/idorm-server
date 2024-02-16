package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedTeamException extends BaseException {

  public AccessDeniedTeamException() {
    super(CalendarResponseCode.ACCESS_DENIED_SLEEPOVER_CALENDAR);
  }
}
