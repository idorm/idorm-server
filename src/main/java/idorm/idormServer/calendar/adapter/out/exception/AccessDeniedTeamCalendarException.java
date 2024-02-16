package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedTeamCalendarException extends BaseException {

  public AccessDeniedTeamCalendarException() {
    super(CalendarResponseCode.ACCESS_DENIED_TEAM_CALENDAR);
  }
}
