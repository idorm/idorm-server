package idorm.idormServer.matchingMate.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.exception.MatchingMateResponseCode;

public class NotFoundDisLikedMemberException extends BaseException {

  public NotFoundDisLikedMemberException() {
    super(MatchingMateResponseCode.NOT_FOUD_DISLIKEDMEMBER);
  }
}
