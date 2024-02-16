package idorm.idormServer.matchingMate.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.exception.MatchingMateResponseCode;

public class NotFoundLikedMemberException extends BaseException {

  public NotFoundLikedMemberException() {
    super(MatchingMateResponseCode.NOT_FOUD_LIKEDMEMBER);
  }
}
