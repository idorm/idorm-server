package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidGenderCharacterException extends BaseException {

  public InvalidGenderCharacterException() {
    super(MatchingInfoResponseCode.INVALID_GENDER_CHARACTER);
  }
}
