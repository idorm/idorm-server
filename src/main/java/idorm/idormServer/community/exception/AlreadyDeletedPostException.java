package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.exception.CommunityResponseCode;

public class AlreadyDeletedPostException extends BaseException {

  public AlreadyDeletedPostException() {
    super(CommunityResponseCode.ALREADY_DELETED_POST);
  }
}
