package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class AccessDeniedCommentException extends BaseException {

  public AccessDeniedCommentException() {
    super(CommunityResponseCode.ACCESS_DENIED_COMMENT);
  }
}
