package idorm.idormServer.community.exception;
import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class AccessDeniedPostException extends BaseException {

  public AccessDeniedPostException() {
    super(CommunityResponseCode.ACCESS_DENIED_POST);
  }
}
