package idorm.idormServer.report.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.report.adapter.out.ReportResponseCode;

public class InvalidReportTypeException extends BaseException {

	public InvalidReportTypeException() {
		super(ReportResponseCode.INVALID_REPORT_TYPE);
	}
}
