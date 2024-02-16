package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundTeamException extends BaseException {

  public NotFoundTeamException() {
    super(CalendarResponseCode.NOT_FOUND_TEAM);
  }
}
