package idorm.idormServer.email.domain;

import static idorm.idormServer.email.domain.EmailStatus.*;

import java.time.LocalDateTime;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.email.adapter.out.api.EmailResponseCode;
import idorm.idormServer.email.adapter.out.api.exception.DuplicatedEmailException;
import idorm.idormServer.email.adapter.out.api.exception.ExpiredEmailVerificationCodeException;
import idorm.idormServer.email.adapter.out.api.exception.UnAuthorizedEmailException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "id")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Email {

	public static final int MAX_EMAIL_LENGTH = 20;
	private static final String EMAIL_REGEX = "^([\\w-]+(?:\\.[\\w-]+)*)+@(inu.ac.kr)$";
	private static final long VALID_VERIFY_MINUTE = 5L;
	private static final long VALID_REGISTER_MINUTE = 10L;

	private Long id;
	private String email;
	private EmailStatus emailStatus;
	private VerificationCode code;
	private LocalDateTime issuedAt;
	private boolean registered;

	public Email(final String email, final VerificationCode code) {
		validateConstructor(email);
		this.email = email;
		this.code = code;
		emailStatus = EmailStatus.SEND;
		registered = false;
		this.issuedAt = LocalDateTime.now();
	}

	private static void validateConstructor(final String value) {
		Validator.validateNotBlank(value);
		Validator.validateFormat(value, EMAIL_REGEX, EmailResponseCode.INVALID_EMAIL_CHARACTER);
	}

	public static Email forMapper(final Long id,
		final String email,
		final EmailStatus emailStatus,
		final VerificationCode code,
		final LocalDateTime issuedAt,
		final boolean registered) {

		return new Email(id, email, emailStatus, code, issuedAt, registered);
	}

	public void assignId(final Long id) {
		this.id = id;
	}

	public void updateVerificationCode(VerificationCode code) {
		validateNotSignUp();
		this.code = code;
		this.issuedAt = LocalDateTime.now();
		this.emailStatus = EmailStatus.SEND;
	}

	public void updateReVerificationCode(VerificationCode code) {
		validateSignUpEmail();
		this.code = code;
		this.issuedAt = LocalDateTime.now();
		this.emailStatus = EmailStatus.RE_SEND;
	}

	public void verify(final String code) {
		validateNotSignUp();
		validateValidTime();

		this.code.match(code);
		this.emailStatus = VERIFIED;
	}

	public void reVerify(final String code) {
		validateSignUpEmail();
		validateValidTime();

		this.code.match(code);
		this.emailStatus = EmailStatus.RE_VERIFIED;
	}

	public void register() {
		if (VERIFIED.isNot(emailStatus)) {
			throw new UnAuthorizedEmailException();
		}
		this.registered = true;
	}

	public void validateReVerified() {
		validateSignUpEmail();
		if (RE_VERIFIED.isNot(emailStatus)) {
			throw new UnAuthorizedEmailException();
		}
		validateValidTime();
	}

	private void validateValidTime() {
		LocalDateTime expiredTime = this.issuedAt.plusMinutes(VALID_REGISTER_MINUTE);
		LocalDateTime now = LocalDateTime.now();

		if (now.isAfter(expiredTime)) {
			throw new ExpiredEmailVerificationCodeException();
		}
	}

	private void validateNotSignUp() {
		if (isRegistered()) {
			throw new DuplicatedEmailException();
		}
	}

	private void validateSignUpEmail() {
		if (!isRegistered()) {
			throw new NotFoundMemberException();
		}
	}
}