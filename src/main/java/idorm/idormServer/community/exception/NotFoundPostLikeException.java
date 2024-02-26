package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class NotFoundPostLikeException extends BaseException {

  public NotFoundPostLikeException() {
    super(CommunityResponseCode.NOT_FOUND_POSTLIKE);
  }
}