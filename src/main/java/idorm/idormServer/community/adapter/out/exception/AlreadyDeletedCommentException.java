package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class AlreadyDeletedCommentException extends BaseException {

  public AlreadyDeletedCommentException() {
    super(CommunityResponseCode.ALREADY_DELETED_COMMENT);
  }
}
