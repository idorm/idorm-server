package idorm.idormServer.member.application.port.out;

import idorm.idormServer.member.domain.Member;

public interface CreateMemberPort {

	Member create(String email, String password, String nickname);
}
