package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidWishTextLengthException extends BaseException {

  public InvalidWishTextLengthException() {
    super(MatchingInfoResponseCode.INVALID_WISHTEXT_LENGTH);
  }
}
