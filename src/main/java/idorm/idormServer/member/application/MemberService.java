package idorm.idormServer.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.application.port.out.DeleteRefreshTokenPort;
import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.email.application.port.out.DeleteEmailPort;
import idorm.idormServer.email.application.port.out.LoadEmailPort;
import idorm.idormServer.email.application.port.out.SaveEmailPort;
import idorm.idormServer.email.entity.Email;
import idorm.idormServer.member.application.port.in.MemberUseCase;
import idorm.idormServer.member.application.port.in.dto.MemberInfoResponse;
import idorm.idormServer.member.application.port.in.dto.NicknameUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.PasswordUpdateRequest;
import idorm.idormServer.member.application.port.in.dto.SignupRequest;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.application.port.out.SaveMemberPort;
import idorm.idormServer.member.application.port.out.WithdrawMemberPort;
import idorm.idormServer.member.entity.Member;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements MemberUseCase {

	private final EncryptPort encryptPort;

	private final LoadEmailPort loadEmailPort;
	private final SaveEmailPort saveEmailPort;
	private final DeleteEmailPort deleteEmailPort;

	private final SaveMemberPort saveMemberPort;
	private final LoadMemberPort loadMemberPort;
	// private final CheckNicknamesPort checkNicknamesPort;
	private final WithdrawMemberPort withdrawMemberPort;

	private final DeleteRefreshTokenPort deleteRefreshTokenPort;

	@Override
	@Transactional
	public void signUp(final SignupRequest request) {
		Email email = loadEmailPort.findByEmail(request.email());

		loadMemberPort.validateUniqueEmail(request.email());
		loadMemberPort.validateUniqueNickname(request.nickname());

		email.register();
		Member member = new Member(request.email(), request.password(), request.nickname(), encryptPort);
		saveMemberPort.save(member);
	}

	@Override
	public MemberInfoResponse getInfo(final AuthResponse auth) {
		Member member = loadMemberPort.loadMember(auth.getId());
		return MemberInfoResponse.of(member);
	}

	@Override
	@Transactional
	public void editNickname(final AuthResponse auth, final NicknameUpdateRequest request) {
		Member member = loadMemberPort.loadMember(auth.getId());

		loadMemberPort.validateUniqueNickname(request.nickname());
		member.updateNickname(request.nickname());
	}

	@Override
	@Transactional
	public void editPassword(final PasswordUpdateRequest request) {
		Email email = loadEmailPort.findByEmail(request.email());
		email.validateReVerified();

		Member member = loadMemberPort.loadMember(request.email());
		member.updatePassword(encryptPort, request.password());
	}

	@Override
	@Transactional
	public void withdraw(final AuthResponse authResponse) {
		Member deleteMember = loadMemberPort.loadMember(authResponse.getId());
		deleteRefreshTokenPort.deleteAll(authResponse.getId());

		Email deleteEmail = loadEmailPort.findByEmail(deleteMember.getEmail());
		deleteEmailPort.delete(deleteEmail);

		// TODO : 타 도메인 삭제 처리

		withdrawMemberPort.withdraw(deleteMember);
	}
}