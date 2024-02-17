package idorm.idormServer.email.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import idorm.idormServer.email.application.port.in.EmailUseCase;
import idorm.idormServer.email.application.port.in.dto.EmailSendRequest;
import idorm.idormServer.email.application.port.in.dto.EmailVerifyRequest;
import idorm.idormServer.email.application.port.out.GenerateVerificationCodePort;
import idorm.idormServer.email.application.port.out.LoadEmailPort;
import idorm.idormServer.email.application.port.out.SaveEmailPort;
import idorm.idormServer.email.application.port.out.SendEmailPort;
import idorm.idormServer.email.domain.Email;
import idorm.idormServer.email.domain.VerificationCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailUseCase {

	private final SaveEmailPort saveEmailPort;
	private final LoadEmailPort loadEmailPort;
	private final GenerateVerificationCodePort verificationCodePort;
	private final SendEmailPort sendEmailPort;

	@Override
	@Transactional
	public void sendVerificationEmail(EmailSendRequest request) {
		Optional<Email> emailWithOptional = loadEmailPort.findByEmailWithOptional(request.email());
		Email email = null;

		if (emailWithOptional.isPresent()) {
			email = emailWithOptional.get();
			email.updateVerificationCode(generateVerificationCode());
		} else {
			email = new Email(request.email(), generateVerificationCode());
		}
		saveEmailPort.save(email);
		sendEmailPort.send(email);
	}

	@Override
	@Transactional
	public void sendReverificationEmail(EmailSendRequest request) {
		Email email = loadEmailPort.findByEmail(request.email());
		email.updateReVerificationCode(generateVerificationCode());

		saveEmailPort.save(email);
		sendEmailPort.send(email);
	}

	@Override
	@Transactional
	public void verifyCode(EmailVerifyRequest request) {
		Email email = loadEmailPort.findByEmail(request.email());
		email.verify(request.code());
	}

	@Transactional
	public void reVerifyCode(EmailVerifyRequest request) {
		Email email = loadEmailPort.findByEmail(request.email());
		email.reVerify(request.code());
	}

	private VerificationCode generateVerificationCode() {
		return verificationCodePort.generate();
	}
}