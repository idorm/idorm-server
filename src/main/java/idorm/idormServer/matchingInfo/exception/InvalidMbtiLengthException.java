package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidMbtiLengthException extends BaseException {

  public InvalidMbtiLengthException() {
    super(MatchingInfoResponseCode.INVALID_MBTI_LENGTH);
  }
}
