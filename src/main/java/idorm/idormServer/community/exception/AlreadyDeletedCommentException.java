package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class AlreadyDeletedCommentException extends BaseException {

  public AlreadyDeletedCommentException() {
    super(CommunityResponseCode.ALREADY_DELETED_COMMENT);
  }
}
