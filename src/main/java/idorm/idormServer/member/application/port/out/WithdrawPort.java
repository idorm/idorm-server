package idorm.idormServer.member.application.port.out;

import idorm.idormServer.email.entity.Email;
import idorm.idormServer.member.entity.Member;

public interface WithdrawPort {

	void withdraw(Member member, Email email);
}