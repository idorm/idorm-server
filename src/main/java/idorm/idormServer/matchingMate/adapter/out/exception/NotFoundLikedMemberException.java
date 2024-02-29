package idorm.idormServer.matchingMate.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingMate.adapter.out.MatchingMateResponseCode;

public class NotFoundLikedMemberException extends BaseException {

	public NotFoundLikedMemberException() {
		super(MatchingMateResponseCode.NOT_FOUND_LIKEDMEMBER);
	}
}
