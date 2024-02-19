package idorm.idormServer.report.domain;

import idorm.idormServer.report.adapter.out.exception.InvalidMemberReportTypeException;
import lombok.Getter;

public enum MemberReason {

	NICKNAME('N'),
	PROFILE_PHOTO('P'),
	MATCHINGINFO('M'),
	ETC('E');

	@Getter
	Character type;

	MemberReason(Character type) {
		this.type = type;
	}

	public static MemberReason from(String memberReason) {
		try {
			return MemberReason.valueOf(memberReason);
		} catch (IllegalArgumentException e) {
			throw new InvalidMemberReportTypeException();
		}
	}

	public static MemberReason valueOf(Character type) {
		MemberReason memberReason = null;
		switch (type) {
			case 'N':
				memberReason = MemberReason.NICKNAME;
				break;
			case 'P':
				memberReason = MemberReason.PROFILE_PHOTO;
				break;
			case 'M':
				memberReason = MemberReason.MATCHINGINFO;
				break;
			case 'E':
				memberReason = MemberReason.ETC;
				break;
		}
		return memberReason;
	}
}
