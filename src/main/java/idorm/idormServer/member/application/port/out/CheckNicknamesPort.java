package idorm.idormServer.member.application.port.out;

public interface CheckNicknamesPort {

	void validateUniqueNickname(String nickname);
}