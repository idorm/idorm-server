package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidAgeLengthException extends BaseException {

  public InvalidAgeLengthException() {
    super(MatchingInfoResponseCode.INVALID_AGE_LENGTH);
  }
}
