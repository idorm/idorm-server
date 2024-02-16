package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class NotFoundMatchingInfoException extends BaseException {
  public NotFoundMatchingInfoException(){
    super(MatchingInfoResponseCode.NOT_FOUND_MATCHINGINFO);
  }
}
