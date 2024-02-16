package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvlidMbtiCharacterException extends BaseException {

  public InvlidMbtiCharacterException() {
    super(MatchingInfoResponseCode.INVALID_MBTI_CHARACTER);
  }
}
