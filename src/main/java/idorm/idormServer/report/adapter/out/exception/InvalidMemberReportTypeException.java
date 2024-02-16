package idorm.idormServer.report.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.report.adapter.out.ReportResponseCode;

public class InvalidMemberReportTypeException extends BaseException {

  public InvalidMemberReportTypeException() {
    super(ReportResponseCode.INVALID_MEMBER_REPORT_TYPE);
  }
}
