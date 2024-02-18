package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class DuplicatedNonFavoriteMateException extends BaseException {

  public DuplicatedNonFavoriteMateException() {
    super(MatchingMateResponseCode.DUPLICATED_DISLIKED_MEMBER);
  }
}
