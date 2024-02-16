package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class NotFoundCommentException extends BaseException {

  public NotFoundCommentException() {
    super(CommunityResponseCode.NOT_FOUND_COMMENT);
  }
}
