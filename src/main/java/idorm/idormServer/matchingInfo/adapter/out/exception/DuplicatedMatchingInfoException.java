package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class DuplicatedMatchingInfoException extends BaseException {

  public DuplicatedMatchingInfoException() {
    super(MatchingInfoResponseCode.DUPLICATED_MATCHINGINFO);
  }
}
