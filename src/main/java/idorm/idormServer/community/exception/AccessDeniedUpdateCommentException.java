package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedUpdateCommentException extends BaseException {

  public AccessDeniedUpdateCommentException() {
    super(CommunityResponseCode.ACCESS_DENIED_UPDATE_COMMENT);
  }
}
