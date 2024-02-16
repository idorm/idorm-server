package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidWakeUpTimeLengthException extends BaseException {

  public InvalidWakeUpTimeLengthException() {
    super(MatchingInfoResponseCode.INVALID_WAKEUPTIME_LENGTH);
  }
}
