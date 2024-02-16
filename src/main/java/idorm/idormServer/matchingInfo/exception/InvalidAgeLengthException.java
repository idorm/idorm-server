package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidAgeLengthException extends BaseException {

  public InvalidAgeLengthException() {
    super(MatchingInfoResponseCode.INVALID_AGE_LENGTH);
  }
}
