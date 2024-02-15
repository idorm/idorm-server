package idorm.idormServer.community.application.port.in;

import idorm.idormServer.member.domain.Member;

public interface LikeUseCase {
	void findLikedPostsByMember(Member member);

	void save(Member member);

	void delete(Member member);
}
