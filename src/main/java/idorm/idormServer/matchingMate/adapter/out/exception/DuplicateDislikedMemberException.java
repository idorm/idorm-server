package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class DuplicateDislikedMemberException extends BaseException {

  public DuplicateDislikedMemberException() {
    super(MatchingMateResponseCode.DUPLICATED_DISLIKED_MEMBER);
  }
}
