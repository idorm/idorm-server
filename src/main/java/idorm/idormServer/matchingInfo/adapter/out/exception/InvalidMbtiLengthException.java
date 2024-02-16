package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidMbtiLengthException extends BaseException {

  public InvalidMbtiLengthException() {
    super(MatchingInfoResponseCode.INVALID_MBTI_LENGTH);
  }
}
