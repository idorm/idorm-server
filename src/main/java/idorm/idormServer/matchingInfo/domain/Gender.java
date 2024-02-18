package idorm.idormServer.matchingInfo.domain;

import idorm.idormServer.matchingInfo.adapter.out.exception.InvalidGenderCharacterException;

public enum Gender {
	MALE,
	FEMALE;

	public static Gender from(String gender) {
		try {
			return Gender.valueOf(gender);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidGenderCharacterException();
		}
	}
}
