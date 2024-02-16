package idorm.idormServer.report.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.report.adapter.out.ReportResponseCode;

public class InvalidCommentReportTypeException extends BaseException {

  public InvalidCommentReportTypeException() {
    super(ReportResponseCode.INVALID_COMMENT_REPORT_TYPE);
  }

}
