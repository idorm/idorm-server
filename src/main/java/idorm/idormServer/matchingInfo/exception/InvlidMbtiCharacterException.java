package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvlidMbtiCharacterException extends BaseException {

  public InvlidMbtiCharacterException() {
    super(MatchingInfoResponseCode.INVALID_MBTI_CHARACTER);
  }
}
