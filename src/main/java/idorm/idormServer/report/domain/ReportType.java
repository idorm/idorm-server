package idorm.idormServer.report.domain;

import idorm.idormServer.report.adapter.out.exception.InvalidReportTypeException;
import lombok.Getter;

public enum ReportType {

	MEMBER('M'),
	POST('P'),
	COMMENT('C');

	@Getter
	Character type;

	ReportType(Character type) {
		this.type = type;
	}

	public static ReportType from(String type) {
		try {
			return ReportType.valueOf(type);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidReportTypeException();
		}
	}

	public static ReportType valueOf(Character type) {
		ReportType reportType = null;
		switch (type) {
			case 'M':
				reportType = ReportType.MEMBER;
				break;
			case 'P':
				reportType = ReportType.POST;
				break;
			case 'C':
				reportType = ReportType.COMMENT;
				break;
		}
		return reportType;
	}

	public static boolean isMemberReport(String type) {
		return from(type).equals(MEMBER);
	}

	public static boolean isPostReport(String type) {
		return from(type).equals(POST);
	}

	public static boolean isCommentReport(String type) {
		return from(type).equals(COMMENT);
	}
}
