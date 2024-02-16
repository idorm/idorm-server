package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class IllegalStatementExploadTeamException extends BaseException {

  public IllegalStatementExploadTeamException() {
    super(CalendarResponseCode.ILLEGAL_STATEMENT_EXPLODEDTEAM);
  }
}
