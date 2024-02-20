package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class AccessDeniedUpdatePostException extends BaseException {

  public AccessDeniedUpdatePostException() {
    super(CommunityResponseCode.ACCESS_DENIED_UPDATE_POST);
  }
}
