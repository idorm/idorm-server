package idorm.idormServer.matchingInfo.exception;

import idorm.idormServer.common.exception.BaseException;

public class IllegalStatementMatchingInfoNonPublicException extends BaseException {

  public IllegalStatementMatchingInfoNonPublicException() {
    super(MatchingInfoResponseCode.ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC);
  }
}
