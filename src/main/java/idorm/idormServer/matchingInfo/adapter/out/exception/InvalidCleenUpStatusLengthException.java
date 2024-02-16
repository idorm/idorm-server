package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidCleenUpStatusLengthException extends BaseException {

  public InvalidCleenUpStatusLengthException() {
    super(MatchingInfoResponseCode.INVALID_CLEENUP_STATUS_LENGTH);
  }
}
