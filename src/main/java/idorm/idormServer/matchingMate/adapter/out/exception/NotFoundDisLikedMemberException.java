package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class NotFoundDisLikedMemberException extends BaseException {

  public NotFoundDisLikedMemberException() {
    super(MatchingMateResponseCode.NOT_FOUD_DISLIKEDMEMBER);
  }
}
