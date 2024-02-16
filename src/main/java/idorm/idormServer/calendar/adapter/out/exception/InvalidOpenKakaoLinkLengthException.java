package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class InvalidOpenKakaoLinkLengthException extends BaseException {

  public InvalidOpenKakaoLinkLengthException() {
    super(CalendarResponseCode.INVALID_OPENKAKAOLINK_LENGTH);
  }
}
