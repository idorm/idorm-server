package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class FieldTargetsRequiredException extends BaseException {

  public FieldTargetsRequiredException() {
    super(CalendarResponseCode.FIELD_TARGETS_REQUIRED);
  }
}
