package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class CannotExplodeTeamException extends BaseException {

  public CannotExplodeTeamException() {
    super(CalendarResponseCode.CANNOT_EXPLODE_TEAM);
  }
}
