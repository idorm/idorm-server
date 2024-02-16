package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidDormCategoryCharacterException extends BaseException {

  public InvalidDormCategoryCharacterException() {
    super(MatchingInfoResponseCode.INVALID_DORMCATEGORY_CHARACTER);
  }
}
