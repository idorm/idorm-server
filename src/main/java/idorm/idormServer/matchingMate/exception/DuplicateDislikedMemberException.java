package idorm.idormServer.matchingMate.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicateDislikedMemberException extends BaseException {

  public DuplicateDislikedMemberException() {
    super(MatchingMateResponseCode.DUPLICATED_DISLIKED_MEMBER);
  }
}
