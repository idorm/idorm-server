package idorm.idormServer.community.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.community.adapter.out.CommunityResponseCode;

public class InvalidTitleLengthException extends BaseException {

  public InvalidTitleLengthException() {
    super(CommunityResponseCode.INVALID_TITLE_LENGTH);
  }
}
