package idorm.idormServer.member.application.port.out;

import java.util.List;

import idorm.idormServer.matchingInfo.entity.DormCategory;
import idorm.idormServer.member.entity.Member;

public interface LoadMemberPort {

	Member loadMember(Long memberId);

	Member loadMember(String email, String password);

	Member loadMember(String email);

	List<Member> loadMembersBy(DormCategory dormCategory);

	List<Member> loadAdmins();

	void validateUniqueNickname(String nickname);

	void validateUniqueEmail(String email);
}