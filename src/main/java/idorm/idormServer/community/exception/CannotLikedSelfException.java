package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class CannotLikedSelfException extends BaseException {

  public CannotLikedSelfException() {
    super(CommunityResponseCode.CANNOT_LIKED_SELF);
  }
}
