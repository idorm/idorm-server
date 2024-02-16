package idorm.idormServer.report.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.report.adapter.out.ReportResponseCode;

public class InvalidCommunityReportTypeException extends BaseException {

  public InvalidCommunityReportTypeException() {
    super(ReportResponseCode.INVALID_COMMUNITY_REPORT_TYPE);
  }

}
