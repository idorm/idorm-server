package idorm.idormServer.auth.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.auth.application.port.in.AuthUseCase;
import idorm.idormServer.auth.application.port.in.dto.AuthResponse;
import idorm.idormServer.auth.application.port.in.dto.LoginRequest;
import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.member.application.port.out.LoadMemberPort;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.notification.application.port.in.dto.RegisterTokenRequest;
import idorm.idormServer.notification.application.port.out.RegisterNotificationTokenPort;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

	private final LoadMemberPort loadMemberPort;
	private final RegisterNotificationTokenPort registerNotificationTokenPort;
	private final EncryptPort encryptorPort;

	@Override
	@Transactional
	public AuthResponse login(final LoginRequest request) {
		Member member = loadMemberPort.loadMember(request.email(), encryptorPort.encrypt(request.password()));
		registerNotificationTokenPort.save(new RegisterTokenRequest(member.getId(), request.fcmToken()));
		return new AuthResponse(member.getId(), member.getRoleType().getName(), member.getNickname().getValue());
	}
}
