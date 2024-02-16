package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidWishTextLengthException extends BaseException {

  public InvalidWishTextLengthException() {
    super(MatchingInfoResponseCode.INVALID_WISHTEXT_LENGTH);
  }
}
