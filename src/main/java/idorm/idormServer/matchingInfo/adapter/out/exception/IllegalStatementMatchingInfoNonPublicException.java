package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class IllegalStatementMatchingInfoNonPublicException extends BaseException {

  public IllegalStatementMatchingInfoNonPublicException() {
    super(MatchingInfoResponseCode.ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC);
  }
}
