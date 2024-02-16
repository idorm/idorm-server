package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class DuplicatedMatchingInfoException extends BaseException {

  public DuplicatedMatchingInfoException() {
    super(MatchingInfoResponseCode.DUPLICATE_MATCHINGINFO);
  }
}
