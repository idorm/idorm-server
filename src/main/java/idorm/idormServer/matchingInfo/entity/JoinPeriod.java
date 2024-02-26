package idorm.idormServer.matchingInfo.entity;

import idorm.idormServer.matchingInfo.adapter.out.exception.InvalidJoinPeriodCharacterException;

public enum JoinPeriod {
	WEEK16,
	WEEK24;

	public static JoinPeriod from(String joinPeriod) {
		try {
			return JoinPeriod.valueOf(joinPeriod);
		} catch (IllegalArgumentException e) {
			throw new InvalidJoinPeriodCharacterException();
		}
	}
}
