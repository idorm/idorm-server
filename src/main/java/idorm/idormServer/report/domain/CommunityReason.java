package idorm.idormServer.report.domain;

import idorm.idormServer.report.adapter.out.exception.InvalidCommunityReportTypeException;
import lombok.Getter;

public enum CommunityReason {

	PLASTER('P'), // 도배
	ADVERTISING_SPAM('S'), // 광고 / 스팸
	OBSCENE_MATERIAL('O'), // 음란물 / 선정성
	ABUSE('A'), // 욕설
	FALSE_INFORMATION('F'), // 사칭 / 허위
	ETC('E');

	@Getter
	Character type;

	CommunityReason(Character type) {
		this.type = type;
	}

	public static CommunityReason from(final String communityReason) {
		try {
			return CommunityReason.valueOf(communityReason);
		} catch (IllegalArgumentException e) {
			throw new InvalidCommunityReportTypeException();
		}
	}

	public static CommunityReason valueOf(Character type) {
		CommunityReason communityReason = null;
		switch (type) {
			case 'P':
				communityReason = CommunityReason.PLASTER;
				break;
			case 'S':
				communityReason = CommunityReason.ADVERTISING_SPAM;
				break;
			case 'O':
				communityReason = CommunityReason.OBSCENE_MATERIAL;
				break;
			case 'A':
				communityReason = CommunityReason.ABUSE;
				break;
			case 'F':
				communityReason = CommunityReason.FALSE_INFORMATION;
				break;
			case 'E':
				communityReason = CommunityReason.ETC;
				break;
		}
		return communityReason;
	}
}
