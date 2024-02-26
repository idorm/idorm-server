package idorm.idormServer.matchingInfo.entity;

import idorm.idormServer.matchingInfo.adapter.out.exception.InvalidDormCategoryCharacterException;

public enum DormCategory {
	DORM1,
	DORM2,
	DORM3;

	public static DormCategory from(String dormCategory) {
		try {
			return DormCategory.valueOf(dormCategory);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidDormCategoryCharacterException();
		}
	}
}
