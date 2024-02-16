package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidGenderCharacterException extends BaseException {

  public InvalidGenderCharacterException() {
    super(MatchingInfoResponseCode.INVALID_GENDER_CHARACTER);
  }
}
