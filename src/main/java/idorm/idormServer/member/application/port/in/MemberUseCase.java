package idorm.idormServer.member.application.port.in;

import idorm.idormServer.auth.domain.AuthInfo;
import idorm.idormServer.member.application.port.in.dto.MemberInfoResponse;
import idorm.idormServer.member.application.port.in.dto.NicknameUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.PasswordUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.SignupRequest;

public interface MemberUseCase {

	void signUp(SignupRequest request);

	MemberInfoResponse getInfo(AuthInfo authInfo);

	void editNickname(AuthInfo authInfo, NicknameUpdateRequest request);

	void editPassword(PasswordUpdateRequest request);

	void leave(AuthInfo authInfo);
}
