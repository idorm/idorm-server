package idorm.idormServer.report.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidCommentReportTypeException extends BaseException {

  public InvalidCommentReportTypeException() {
    super(ReportResponseCode.INVALID_COMMENT_REPORT_TYPE);
  }

}
