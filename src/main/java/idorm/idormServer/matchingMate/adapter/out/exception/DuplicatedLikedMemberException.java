package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class DuplicatedLikedMemberException extends BaseException {

  public DuplicatedLikedMemberException() {
    super(MatchingMateResponseCode.DUPLICATED_LIKED_MEMBER);
  }
}
