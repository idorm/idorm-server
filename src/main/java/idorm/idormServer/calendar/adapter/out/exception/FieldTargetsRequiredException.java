package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class FieldTargetsRequiredException extends BaseException {

  public FieldTargetsRequiredException() {
    super(CalendarResponseCode.FIELD_TARGETS_REQUIRED);
  }
}
