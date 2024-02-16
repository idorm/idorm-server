package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidShowerTimeLengthException extends BaseException {

  public InvalidShowerTimeLengthException() {
    super(MatchingInfoResponseCode.INVALID_SHOWERTIME_LENGTH);
  }
}
