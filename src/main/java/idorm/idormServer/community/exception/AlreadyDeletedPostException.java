package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class AlreadyDeletedPostException extends BaseException {

  public AlreadyDeletedPostException() {
    super(CommunityResponseCode.ALREADY_DELETED_POST);
  }
}
