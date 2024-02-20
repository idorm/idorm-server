package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class CannotLikedSelfException extends BaseException {

  public CannotLikedSelfException() {
    super(CommunityResponseCode.CANNOT_LIKED_SELF);
  }
}
