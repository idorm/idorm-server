package idorm.idormServer.matchingMate.domain;

import idorm.idormServer.matchingMate.adapter.out.exception.InvalidMatePreferenceCharacterException;

public enum MatePreferenceType {
	FAVORITE,
	NONFAVORITE;

	public static MatePreferenceType from(String preference) {
		try {
			return MatePreferenceType.valueOf(preference);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new InvalidMatePreferenceCharacterException();
		}
	}

	public static boolean isFavorite(String preference) {
		return FAVORITE.equals(from(preference));
	}
}
