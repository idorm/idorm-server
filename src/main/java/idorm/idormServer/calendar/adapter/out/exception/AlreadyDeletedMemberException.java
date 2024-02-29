package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class AlreadyDeletedMemberException extends BaseException {

  public AlreadyDeletedMemberException() {
    super(CalendarResponseCode.ALREADY_DELETED_MEMBER);
  }

}
