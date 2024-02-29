package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class IllegalStatementMatchingInfoNonPublicException extends BaseException {

	public IllegalStatementMatchingInfoNonPublicException() {
		super(MatchingMateResponseCode.ILLEGAL_STATEMENT_MATCHINGINFO_NON_PUBLIC);
	}
}
