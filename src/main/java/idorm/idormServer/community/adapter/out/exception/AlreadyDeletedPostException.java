package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class AlreadyDeletedPostException extends BaseException {

  public AlreadyDeletedPostException() {
    super(CommunityResponseCode.ALREADY_DELETED_POST);
  }
}
