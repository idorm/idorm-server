package idorm.idormServer.member.application.port.out;

import idorm.idormServer.member.entity.Member;

public interface LoadMemberPort {

	Member loadMember(Long memberId);

	Member loadMember(String email, String password);

	Member loadMember(String email);

	void validateUniqueNickname(String nickname);

	void validateUniqueEmail(String email);
}