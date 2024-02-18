package idorm.idormServer.member.application.port.in.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.member.domain.Member;
import idorm.idormServer.member.domain.Nickname;
import idorm.idormServer.member.domain.Password;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class SignupRequest {

	private final EncryptPort encryptPort;

	@NotBlank(message = "이메일은 공백일 수 없습니다.")
	String email;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	String password;

	@NotBlank(message = "닉네임은 공백일 수 없습니다.")
	String nickname;

	public Member from() {
		return Member.builder()
			.email(email)
			.password(Password.from(encryptPort.encrypt(password)))
			.nickname(Nickname.from(nickname))
			.build();
	}
}