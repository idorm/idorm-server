package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class CannotRegisterTeamStatusFullException extends BaseException {

  public CannotRegisterTeamStatusFullException() {
    super(CalendarResponseCode.CANNOT_REGISTER_TEAM_STATUS_FULL);
  }
}
