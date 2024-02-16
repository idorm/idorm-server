package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class NotFoundTeamCalendarException extends BaseException {

  public NotFoundTeamCalendarException() {
    super(CalendarResponseCode.NOT_FOUND_TEAM_CALENDAR);
  }
}
