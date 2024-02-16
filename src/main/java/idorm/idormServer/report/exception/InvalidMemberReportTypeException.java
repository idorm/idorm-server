package idorm.idormServer.report.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidMemberReportTypeException extends BaseException {

  public InvalidMemberReportTypeException() {
    super(ReportResponseCode.INVALID_MEMBER_REPORT_TYPE);
  }
}
