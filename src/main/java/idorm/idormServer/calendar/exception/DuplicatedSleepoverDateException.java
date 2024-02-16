package idorm.idormServer.calendar.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedSleepoverDateException extends BaseException {

  public DuplicatedSleepoverDateException() {
    super(CalendarResponseCode.DUPLICATED_SLEEPOVER_DATE);
  }
}
