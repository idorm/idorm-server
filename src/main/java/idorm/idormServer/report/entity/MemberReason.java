package idorm.idormServer.report.entity;

import idorm.idormServer.report.adapter.out.exception.InvalidMemberReportTypeException;

public enum MemberReason {

	NICKNAME,
	PROFILE_PHOTO,
	MATCHINGINFO,
	ETC;

	public static MemberReason from(String memberReason) {
		try {
			return MemberReason.valueOf(memberReason);
		} catch (IllegalArgumentException e) {
			throw new InvalidMemberReportTypeException();
		}
	}
}
