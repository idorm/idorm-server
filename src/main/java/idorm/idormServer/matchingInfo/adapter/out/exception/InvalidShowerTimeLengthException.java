package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidShowerTimeLengthException extends BaseException {

  public InvalidShowerTimeLengthException() {
    super(MatchingInfoResponseCode.INVALID_SHOWERTIME_LENGTH);
  }
}
