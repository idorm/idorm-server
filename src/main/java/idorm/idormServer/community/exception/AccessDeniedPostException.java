package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class AccessDeniedPostException extends BaseException {

  public AccessDeniedPostException() {
    super(CommunityResponseCode.ACCESS_DENIED_POST);
  }
}
