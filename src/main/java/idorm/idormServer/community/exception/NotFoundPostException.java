package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class NotFoundPostException extends BaseException {

  public NotFoundPostException() {
    super(CommunityResponseCode.NOT_FOUND_POST);
  }
}
