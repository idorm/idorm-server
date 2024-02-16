package idorm.idormServer.report.exception;

import idorm.idormServer.common.exception.BaseException;

public class InvalidCommunityReportTypeException extends BaseException {

  public InvalidCommunityReportTypeException() {
    super(ReportResponseCode.INVALID_COMMUNITY_REPORT_TYPE);
  }

}
