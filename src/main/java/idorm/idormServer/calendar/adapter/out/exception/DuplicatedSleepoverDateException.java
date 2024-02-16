package idorm.idormServer.calendar.adapter.out.exception;

import idorm.idormServer.calendar.adapter.out.CalendarResponseCode;
import idorm.idormServer.common.exception.BaseException;

public class DuplicatedSleepoverDateException extends BaseException {

  public DuplicatedSleepoverDateException() {
    super(CalendarResponseCode.DUPLICATED_SLEEPOVER_DATE);
  }
}
