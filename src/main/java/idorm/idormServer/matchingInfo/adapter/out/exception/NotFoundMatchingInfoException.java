package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class NotFoundMatchingInfoException extends BaseException {
  public NotFoundMatchingInfoException(){
    super(MatchingInfoResponseCode.NOT_FOUND_MATCHINGINFO);
  }
}
