package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class NotFoundTeamMemberException extends BaseException {

  public NotFoundTeamMemberException() {
    super(CalendarResponseCode.NOT_FOUND_TEAM_MEMBER);
  }
}
