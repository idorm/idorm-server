package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class CannotLikedSelfException extends BaseException {

  public CannotLikedSelfException() {
    super(CommunityResponseCode.CANNOT_LIKED_SELF);
  }
}
