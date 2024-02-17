package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class NotFoundPostException extends BaseException {

  public NotFoundPostException() {
    super(CommunityResponseCode.NOT_FOUND_POST);
  }
}
