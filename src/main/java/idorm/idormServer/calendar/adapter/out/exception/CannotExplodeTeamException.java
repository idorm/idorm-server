package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class CannotExplodeTeamException extends BaseException {

  public CannotExplodeTeamException() {
    super(CalendarResponseCode.CANNOT_EXPLODE_TEAM);
  }
}
