package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class NotFoundTeamException extends BaseException {

  public NotFoundTeamException() {
    super(CalendarResponseCode.NOT_FOUND_TEAM);
  }
}
