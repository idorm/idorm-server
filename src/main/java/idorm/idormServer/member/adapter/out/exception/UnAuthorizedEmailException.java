package idorm.idormServer.member.adapter.out.exception;

import idorm.idormServer.common.exception.BaseException;
import idorm.idormServer.member.adapter.out.MemberResponseCode;

public class UnAuthorizedEmailException extends BaseException {

	public UnAuthorizedEmailException() {
		super(MemberResponseCode.UNAUTHORIZED_EMAIL);
	}
}
