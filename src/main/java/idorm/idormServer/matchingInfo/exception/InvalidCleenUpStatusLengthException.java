package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidCleenUpStatusLengthException extends BaseException {

  public InvalidCleenUpStatusLengthException() {
    super(MatchingInfoResponseCode.INVALID_CLEENUP_STATUS_LENGTH);
  }
}
