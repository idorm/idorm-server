package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidWakeUpTimeLengthException extends BaseException {

  public InvalidWakeUpTimeLengthException() {
    super(MatchingInfoResponseCode.INVALID_WAKEUPTIME_LENGTH);
  }
}
