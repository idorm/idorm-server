package idorm.idormServer.report.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.report.adapter.out.ReportResponseCode;

public class InvalidTargetSelfException extends BaseException {

	public InvalidTargetSelfException() {
		super(ReportResponseCode.INVALID_TARGET_SELF);
	}
}
