package idorm.idormServer.email.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.email.adapter.out.EmailResponseCode;

public class InvalidVerificationCode extends BaseException {

	public InvalidVerificationCode() {
		super(EmailResponseCode.INVALID_VERIFICATION_CODE);
	}
}
