package idorm.idormServer.matchingMate.application.port.out;

import idorm.idormServer.member.entity.Member;

public interface DeleteMatchingMatePort {

	void deleteByMemberWithdrawn(Member member);
}