package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class DuplicatedFavoriteMateException extends BaseException {

  public DuplicatedFavoriteMateException() {
    super(MatchingMateResponseCode.DUPLICATED_LIKED_MEMBER);
  }
}
