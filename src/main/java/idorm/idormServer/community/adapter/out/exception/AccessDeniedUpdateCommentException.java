package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class AccessDeniedUpdateCommentException extends BaseException {

  public AccessDeniedUpdateCommentException() {
    super(CommunityResponseCode.ACCESS_DENIED_UPDATE_COMMENT);
  }
}
