package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidOpenKakaoLinkLengthException extends BaseException {

  public InvalidOpenKakaoLinkLengthException() {
    super(CalendarResponseCode.INVALID_OPENKAKAOLINK_LENGTH);
  }
}
