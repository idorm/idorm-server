package idorm.idormServer.email.entity;

import static idorm.idormServer.email.entity.EmailStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import idorm.idormServer.common.util.Validator;
import idorm.idormServer.email.adapter.out.EmailResponseCode;
import idorm.idormServer.email.adapter.out.exception.DuplicatedEmailException;
import idorm.idormServer.email.adapter.out.exception.ExpiredEmailVerificationCodeException;
import idorm.idormServer.email.adapter.out.exception.InvalidVerificationCode;
import idorm.idormServer.member.adapter.out.exception.UnAuthorizedEmailException;
import idorm.idormServer.member.adapter.out.exception.NotFoundMemberException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Email {

	public static final int MAX_EMAIL_LENGTH = 20;
	private static final String EMAIL_REGEX = "^([\\w-]+(?:\\.[\\w-]+)*)+@(inu.ac.kr)$";
	public static final int CODE_LENGTH = 7;
	private static final String CODE_REGEX = "^\\d{3}-\\d{3}$";

	private static final long VALID_VERIFY_MINUTE = 5L;
	private static final long VALID_REGISTER_MINUTE = 10L;

	@Id
	@Column(name = "email_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = MAX_EMAIL_LENGTH)
	private String email;

	@Column(nullable = false, length = CODE_LENGTH)
	private String code;

	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime issuedAt;

	@Column(nullable = false)
	private boolean registered;

	@Enumerated(value = EnumType.STRING)
	@Column(columnDefinition = "ENUM('SEND', 'VERIFIED', 'RE_SEND', 'RE_VERIFIED')")
	private EmailStatus emailStatus;

	public Email(final String email, final String code) {
		validateConstructor(email, code);
		this.email = email;
		this.code = code;
		emailStatus = EmailStatus.SEND;
		registered = false;
		this.issuedAt = LocalDateTime.now();
	}

	private static void validateConstructor(final String email, final String code) {
		Validator.validateNotBlank(List.of(email, code));
		Validator.validateFormat(email, EMAIL_REGEX, EmailResponseCode.INVALID_EMAIL_CHARACTER);
	}

	public void updateVerificationCode(String code) {
		validateNotSignUp();
		this.code = code;
		this.issuedAt = LocalDateTime.now();
		this.emailStatus = EmailStatus.SEND;
	}

	public void updateReVerificationCode(String code) {
		validateSignUpEmail();
		this.code = code;
		this.issuedAt = LocalDateTime.now();
		this.emailStatus = EmailStatus.RE_SEND;
	}

	public void verify(final String code) {
		validateNotSignUp();
		validateValidTime();

		matchCode(code);
		this.emailStatus = VERIFIED;
	}

	public void reVerify(final String code) {
		validateSignUpEmail();
		validateValidTime();

		matchCode(code);
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

	private void matchCode(String inputCode) {
		Validator.validateNotBlank(inputCode);
		Validator.validateFormat(inputCode, CODE_REGEX, EmailResponseCode.INVALID_VERIFICATION_CODE);

		if (!this.code.equals(inputCode)) {
			throw new InvalidVerificationCode();
		}
	}
}