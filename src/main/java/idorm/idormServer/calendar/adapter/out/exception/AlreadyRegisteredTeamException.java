package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class AlreadyRegisteredTeamException extends BaseException {

  public AlreadyRegisteredTeamException() {
    super(CalendarResponseCode.ALREADY_REGISTERED_TEAM);
  }
}
