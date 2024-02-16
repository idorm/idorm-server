package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class NotFoundPostLikeException extends BaseException {

  public NotFoundPostLikeException() {
    super(CommunityResponseCode.NOT_FOUND_POSTLIKE);
  }
}
