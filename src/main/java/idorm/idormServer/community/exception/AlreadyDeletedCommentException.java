package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class AlreadyDeletedCommentException extends BaseException {

  public AlreadyDeletedCommentException() {
    super(CommunityResponseCode.ALREADY_DELETED_COMMENT);
  }
}
