package idorm.idormServer.member.application.port.out;

import idorm.idormServer.member.domain.Member;

public interface SaveMemberPort {

	void save(Member member);
}
