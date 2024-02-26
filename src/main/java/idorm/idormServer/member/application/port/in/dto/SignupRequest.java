package idorm.idormServer.member.application.port.in.dto;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

import idorm.idormServer.auth.application.port.out.EncryptPort;
import idorm.idormServer.member.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Component
@RequiredArgsConstructor
public class SignupRequest {

	@NotBlank(message = "이메일은 공백일 수 없습니다.")
	String email;

	@NotBlank(message = "비밀번호는 공백일 수 없습니다.")
	String password;

	@NotBlank(message = "닉네임은 공백일 수 없습니다.")
	String nickname;

	public Member from(final EncryptPort encryptPort, final String encryptedPassword) {
		return Member.builder()
			.email(email)
			.password(encryptedPassword)
			.nickname(nickname)
			.build();
	}
}