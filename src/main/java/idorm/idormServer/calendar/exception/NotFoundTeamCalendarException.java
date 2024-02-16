package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundTeamCalendarException extends BaseException {

  public NotFoundTeamCalendarException() {
    super(CalendarResponseCode.NOT_FOUND_TEAM_CALENDAR);
  }
}
