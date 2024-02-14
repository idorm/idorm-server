package idorm.idormServer.member.application.port.in;

import idorm.idormServer.auth.dto.AuthInfo;
import idorm.idormServer.member.application.port.in.dto.LoginRequest;
import idorm.idormServer.member.application.port.in.dto.MemberProfileResponse;
import idorm.idormServer.member.application.port.in.dto.NicknameUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.PasswordUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.SignupRequest;
import idorm.idormServer.member.domain.Member;

public interface MemberUseCase {

    void signUp(SignupRequest request);

    Long login(LoginRequest request);

    void editNickname(Member member, NicknameUpdateRequest request);

    void editPassword(PasswordUpdateRequest request);

    void leave(Member member);
}
