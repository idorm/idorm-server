package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedTeamCalendarException extends BaseException {

  public AccessDeniedTeamCalendarException() {
    super(CalendarResponseCode.ACCESS_DENIED_TEAM_CALENDAR);
  }
}
