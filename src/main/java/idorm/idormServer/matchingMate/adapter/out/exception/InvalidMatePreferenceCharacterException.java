package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class InvalidMatePreferenceCharacterException extends BaseException {
	public InvalidMatePreferenceCharacterException() {
		super(MatchingMateResponseCode.INVALID_MATE_PREFERENCE_CHARACTER);
	}
}
