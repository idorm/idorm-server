package idorm.idormServer.matchingInfo.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.matchingInfo.adapter.out.MatchingInfoResponseCode;

public class InvalidOpenKakaoLinkLengthException extends BaseException {

	public InvalidOpenKakaoLinkLengthException() {
		super(MatchingInfoResponseCode.INVALID_OPENKAKAOLINK_LENGTH);
	}
}
