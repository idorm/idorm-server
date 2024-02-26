package idorm.idormServer.member.application.port.out;

import idorm.idormServer.member.entity.Member;

public interface WithdrawMemberPort {

	void withdraw(Member member);
}