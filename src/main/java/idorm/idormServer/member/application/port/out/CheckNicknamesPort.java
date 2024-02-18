package idorm.idormServer.member.application.port.out;

import idorm.idormServer.member.domain.Nickname;

public interface CheckNicknamesPort {

	void validateUniqueNickname(Nickname nickname);
}