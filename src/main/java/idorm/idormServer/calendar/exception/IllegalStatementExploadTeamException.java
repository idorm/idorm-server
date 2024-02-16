package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class IllegalStatementExploadTeamException extends BaseException {

  public IllegalStatementExploadTeamException() {
    super(CalendarResponseCode.ILLEGAL_STATEMENT_EXPLODEDTEAM);
  }
}
