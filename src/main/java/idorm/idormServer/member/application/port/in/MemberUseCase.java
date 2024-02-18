package idorm.idormServer.member.application.port.in;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.member.application.port.in.dto.MemberInfoResponse;
import idorm.idormServer.member.application.port.in.dto.NicknameUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.PasswordUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.SignupRequest;

public interface MemberUseCase {

	void signUp(SignupRequest request);

	MemberInfoResponse getInfo(AuthResponse authResponse);

	void editNickname(AuthResponse authResponse, NicknameUpdateRequest request);

	void editPassword(PasswordUpdateRequest request);

	void withdraw(AuthResponse authResponse);
}
