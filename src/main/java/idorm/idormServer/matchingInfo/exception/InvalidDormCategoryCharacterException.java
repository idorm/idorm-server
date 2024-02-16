package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidDormCategoryCharacterException extends BaseException {

  public InvalidDormCategoryCharacterException() {
    super(MatchingInfoResponseCode.INVALID_DORMCATEGORY_CHARACTER);
  }
}
