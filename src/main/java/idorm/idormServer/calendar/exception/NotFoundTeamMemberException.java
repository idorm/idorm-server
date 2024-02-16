package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundTeamMemberException extends BaseException {

  public NotFoundTeamMemberException() {
    super(CalendarResponseCode.NOT_FOUND_TEAM_MEMBER);
  }
}
