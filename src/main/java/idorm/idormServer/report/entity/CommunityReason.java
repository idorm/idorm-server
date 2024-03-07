package idorm.idormServer.report.entity;

import idorm.idormServer.report.adapter.out.exception.InvalidCommunityReportTypeException;

public enum CommunityReason {

	PLASTER, // 도배
	ADVERTISING_SPAM, // 광고, 스팸
	OBSCENE_MATERIAL, // 음란물, 선정성
	ABUSE, // 욕설
	FALSE_INFORMATION, // 사칭, 허위
	ETC;

	public static CommunityReason from(final String communityReason) {
		try {
			return CommunityReason.valueOf(communityReason);
		} catch (IllegalArgumentException e) {
			throw new InvalidCommunityReportTypeException();
		}
	}
}
