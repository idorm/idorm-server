package idorm.idormServer.member.adapter.out.persistence;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.member.application.port.out.CreateMemberPort;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.domain.Nickname;
import idorm.idormServer.member.domain.Password;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateMemberAdaptor implements CreateMemberPort {

	private final EncryptPort encryptPort;

	@Override
	public Member create(String email, String password, String nickname) {
		return Member.builder()
			.email(email)
			.password(Password.from(encryptPort.encrypt(password)))
			.nickname(Nickname.from(nickname))
			.build();
	}
}
