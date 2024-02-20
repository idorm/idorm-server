package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class IllegalArgumentDateSetException extends BaseException {

  public IllegalArgumentDateSetException() {
    super(CalendarResponseCode.ILLEGAL_ARGUMENT_DATE_SET);
  }

}
