package idorm.idormServer.matchingMate.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedLikedMemberException extends BaseException {

  public DuplicatedLikedMemberException() {
    super(MatchingMateResponseCode.DUPLICATED_LIKED_MEMBER);
  }
}
