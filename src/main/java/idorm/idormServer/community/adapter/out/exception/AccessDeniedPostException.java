package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class AccessDeniedPostException extends BaseException {

  public AccessDeniedPostException() {
    super(CommunityResponseCode.ACCESS_DENIED_POST);
  }
}
