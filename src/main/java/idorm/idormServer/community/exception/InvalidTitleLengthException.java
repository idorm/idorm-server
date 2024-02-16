package idorm.idormServer.community.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidTitleLengthException extends BaseException {

  public InvalidTitleLengthException() {
    super(CommunityResponseCode.INVALID_TITLE_LENGTH);
  }
}
